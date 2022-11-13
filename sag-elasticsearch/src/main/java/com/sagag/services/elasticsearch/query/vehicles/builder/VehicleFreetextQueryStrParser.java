package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.utils.QueryUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class VehicleFreetextQueryStrParser {

  private static final String OR_OPERAND = QueryUtils.OR_OPERAND;

  private static final String DOT_ENGINE_CODE_DELIMITER = ".";

  private static final String COMMA_ENGINE_CODE_DELIMITER = ",";

  private static Map<String, String> swappedDelimiterMap;

  static {
    swappedDelimiterMap = new HashMap<>();
    swappedDelimiterMap.put(DOT_ENGINE_CODE_DELIMITER, COMMA_ENGINE_CODE_DELIMITER);
    swappedDelimiterMap.put(COMMA_ENGINE_CODE_DELIMITER, DOT_ENGINE_CODE_DELIMITER);
  }

  public String parseQueryContainEngineCode(String originalTxt) {
    if (StringUtils.isBlank(originalTxt)
        || StringUtils.contains(originalTxt, OR_OPERAND)) {
      return StringUtils.defaultIfBlank(originalTxt, StringUtils.EMPTY);
    }
    return Stream.of(StringUtils.split(originalTxt, StringUtils.SPACE))
        .map(queryStrParser()).collect(Collectors.joining(StringUtils.SPACE));
  }

  private Predicate<String> containEngineCodeDelimiterPredicate(String str) {
    return delimiter -> StringUtils.contains(str, delimiter);
  }

  private Function<String, String> queryStrParser() {
    return query -> {
      final StringBuilder strBuilder = new StringBuilder();
      swappedDelimiterMap.forEach((delimiter, swapDelimiter) -> {
        if (containEngineCodeDelimiterPredicate(query).test(delimiter)) {
          strBuilder.append(query).append(OR_OPERAND)
          .append(StringUtils.replace(query, delimiter, swapDelimiter));
        }
      });
      if (strBuilder.length() == 0) {
        strBuilder.append(query);
      }
      return strBuilder.toString();
    };
  }

}
