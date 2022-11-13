package com.sagag.services.elasticsearch.parser;

import com.sagag.services.elasticsearch.utils.FreeTextNormalizeStringUtils;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;

@Component
public class OeAndArtNumStringParser implements ISearchStringParser {

  @Override
  public String apply(String text, Object[] objs) {
    final Collection<Function<String, String>> parsers = QueryUtils.getEsSpecialCharacterParsers();
    String modifiedTxt = applyParsers(text, parsers);
    parsers.add(FreeTextNormalizeStringUtils::stripLeadingZerosOrEmpty);
    return FreeTextNormalizeStringUtils.or(applyParsers(text, parsers), modifiedTxt);
  }

}