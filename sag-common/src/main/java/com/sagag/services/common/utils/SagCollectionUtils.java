package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class SagCollectionUtils {

  /**
   * Distinct the list by properties.
   * This will be used in stream filter.
   *
   * @param keyExtractors list of T properties
   * @param <T>           class
   * @return the T class predicate
   */
  @SafeVarargs
  public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
    final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
    return t -> {
      final List<?> keys = Arrays.stream(keyExtractors)
          .map(ke -> ke.apply(t))
          .collect(Collectors.toList());
      return seen.putIfAbsent(keys, Boolean.TRUE) == null;
    };
  }

  public static Map<String, String> convertOptionalParametersStringToMap(
      String optionalParameters) {
    if (StringUtils.isBlank(optionalParameters)) {
      return Collections.emptyMap();
    }
    return Arrays.stream(StringUtils.split(optionalParameters, SagConstants.COMMA_NO_SPACE))
        .map(entry -> StringUtils.split(entry, SagConstants.EQUAL))
        .filter(entries -> ArrayUtils.getLength(entries) > 1)
        .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
  }
}
