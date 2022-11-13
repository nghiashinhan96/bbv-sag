package com.sagag.services.elasticsearch.parser;

import com.sagag.services.elasticsearch.utils.FreeTextNormalizeStringUtils;
import com.sagag.services.elasticsearch.utils.QueryUtils;
import java.util.Collection;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OnlyArticleNrStringParser implements ISearchStringParser {

  @Override
  public String apply(String text, Object[] objs) {
    final Collection<Function<String, String>> parsers = QueryUtils.getEsSpecialCharacterParsers();
    parsers.add(FreeTextNormalizeStringUtils::buildPerfectMatchConditionWithoutStripZero);
    parsers.add(StringUtils::lowerCase);
    String modifiedTxt = applyParsers(text, parsers);
    parsers.add(FreeTextNormalizeStringUtils::stripLeadingZerosOrEmpty);

    return FreeTextNormalizeStringUtils.or(applyParsers(text, parsers), modifiedTxt);
  }

}
