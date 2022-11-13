package com.sagag.services.elasticsearch.parser;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ISearchStringParser extends BiFunction<String, Object[], String> {

  @Override
  String apply(String text, Object[] objs);

  default String applyParsers(String text, Collection<Function<String, String>> parsers) {
    if (StringUtils.isBlank(text) || CollectionUtils.isEmpty(parsers)) {
      return StringUtils.defaultString(text);
    }
    String str = text;
    for (Function<String, String> parser : parsers) {
      str = parser.apply(str);
    }
    return StringUtils.defaultString(str);
  }

}
