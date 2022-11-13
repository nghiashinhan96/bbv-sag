package com.sagag.services.tools.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Utilities for default values.
 */
@UtilityClass
public final class DefaultUtils {

  public static Long defaultLong(Long value) {
    return value == null ? NumberUtils.LONG_ZERO : value.longValue();
  }

  public static Long defaultLong(Long value, Long defaultValue) {
    return value == null ? defaultValue : value;
  }

  public static Integer defaultInt(Integer value, Integer defaultValue) {
    return value == null ? defaultValue : value.intValue();
  }

  public static Integer defaultInt(Long value, Integer defaultValue) {
    return value == null ? defaultValue : value.intValue();
  }

  public static Integer defaultInt(Long value) {
    return value == null ? null : value.intValue();
  }

  public static Double defaultDouble(Double value) {
    return value == null ? NumberUtils.DOUBLE_ZERO : value.doubleValue();
  }

  public static Long defaultModifiedUserId(Long modifiedUserId) {
    return modifiedUserId == null || modifiedUserId == NumberUtils.LONG_ZERO ? null : modifiedUserId;
  }

  public static String toUtf8Value(String value) {
    if (Charset.defaultCharset() != StandardCharsets.UTF_8) {
      return value;
    }
    if (value == null) {
      return null;
    }
    if (StringUtils.isBlank(value)) {
      return StringUtils.EMPTY;
    }
    return new String(value.getBytes(), StandardCharsets.UTF_8);
  }

  public static String handleNullStr(final String value) {
    if (StringUtils.isBlank(value)
      || StringUtils.equalsIgnoreCase(ToolConstants.NULL_STR, value)) {
      return null;
    }
    return value;
  }

  public static Boolean handleBooleanStr(final String value) {
    String str = handleNullStr(value);
    if (str == null) {
      return null;
    }
    return BooleanUtils.toBoolean(NumberUtils.toInt(str));
  }

}
