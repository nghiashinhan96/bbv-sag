package com.sagag.services.elasticsearch.parser;

import com.sagag.services.elasticsearch.utils.FreeTextNormalizeStringUtils;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Component
public class FreetextStringParser implements ISearchStringParser {

  @Override
  public String apply(String freetext, Object[] objs) {

    boolean perfectMatch = false;
    if (!ArrayUtils.isEmpty(objs)) {
      perfectMatch = BooleanUtils.isTrue((Boolean) objs[0]);
    }

    final List<Function<String, String>> parsers = new LinkedList<>();
    parsers.add(StringUtils::lowerCase);
    if (perfectMatch) {
      parsers.add(FreeTextNormalizeStringUtils::buildPerfectMatchConditionWithoutStripZero);
    } else {
      parsers.add(QueryUtils::removeNonAlphaCharsExcluded);
      parsers.add(FreeTextNormalizeStringUtils::handleExcludedChar);
    }
    parsers.add(QueryUtils::replaceEsSlashCharacter);
    return applyParsers(freetext, parsers);
  
  }

}
