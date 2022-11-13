package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Objects;

@UtilityClass
public final class SagStringUtils {

  public static String stripNonAlphaNumericChars(final String val) {
    return val.replaceAll("[\\W]|_", "");
  }

  /**
   * Returns the elastic search "null" string value instead of blank.
   *
   * @param val the lowercase blank value.
   * @return the value
   */
  public static String handleElasticBlank(final String val) {
    // the default term matching is lower case
    return StringUtils.isBlank(val) ? SagConstants.NULL_STR : val;
  }

  /**
   * Returns empty <code>String</code> value when the <code>val</code> is 'null' value.
   *
   * @param val the input value to check
   * @return empty <code>String</code> is 'null', <code>val</code> otherwise.
   */
  public static String toEmptyIfNullValue(final String val) {
    return StringUtils.equals(SagConstants.NULL_STR, val) ? StringUtils.EMPTY : val;
  }

  /**
   * Checks empty <code>StringBuilder</code>
   *
   * <pre>
   * If string builder is null or empty is true
   * If string builder has text is false
   * and string builder just contains <code>StringUtils.SPACE</code> character is false
   * </pre>
   *
   * @param strBuilder the input StringBuilder val
   * @return {@link true} if the length of string builder is zero
   */
  public static boolean isEmptyStrBuilder(final StringBuilder strBuilder) {
    return Objects.isNull(strBuilder) || NumberUtils.INTEGER_ZERO == strBuilder.length();
  }

  /**
   * Returns the string value for comparing.
   *
   * <pre>
   * Refer to EBL source code: StringFormatterUtility.prepareForCompare
   * </pre>
   */
  public static String prepareForCompare(final String str) {
    return StringUtils.defaultString(str).toLowerCase().replace("ä", "a").replace("ö", "o")
        .replace("ü", "u").replace("ß", "s");
  }

  /**
   * Check whether the sources contains ignore case the target.
   *
   * @param sources
   * @param target
   * @return
   */
  public static boolean containsIgnoreCase(List<String> sources, String target) {
    return !CollectionUtils.isEmpty(sources)
        && sources.stream().anyMatch(item -> item.equalsIgnoreCase(target));
  }

}
