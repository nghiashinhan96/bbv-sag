package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.LanguageCode;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class QueryUtils {

  private static final float DF_BOOST = ElasticsearchConstants.DEFAULT_BOOST;

  private static final String UNICODE_DE =
      "\\u00e4\\u00fc\\u00f6\\u00df\\u00e9\\u00e8\\u00e0\\u00f9\\u00e2\\u00ea\\u00ee\\u00f4\\u00fb\\u00eb\\u00ef\\u00e7";

  private static final String UNICODE_CS =
      "\\u00e1\\u010d\\u010f\\u00e9\\u011b\\u00ed\\u0148\\u00f3\\u0159\\u0161\\u0165\\u00fa\\u016f\\u00fd\\u017e";

  private static final String UNICODE_SR = "\\u010d\\u0107\\u0161\\u017e\\u0111";

  private static final String NON_ALPHA_CHARS_PATTERN = "[^A-Za-z0-9 \\\\* s" + UNICODE_DE + "]";

  private static final String NON_ALPHA_CHARS_PATTERN_CS_RO_SR_HU =
      "[^A-Za-z0-9 \\\\* s" + UNICODE_DE + UNICODE_CS + UNICODE_SR + "]";

  // The pattern is the same with non alpha chars pattern but excluded hyphen(-) and dot(.)
  public static final String NON_ALPHA_CHARS_EXCLUDED_PATTERN = "[^-A-Za-z0-9 \\\\* s" + UNICODE_DE + "\\.]";
  public static final String NON_ALPHA_CHARS_EXCLUDED_PATTERN_CS = "[^-A-Za-z0-9 \\\\* s" + UNICODE_CS + "\\.]";

  protected static final String[] EXCLUDED_CHAR = { SagConstants.HYPHEN, SagConstants.DOT };

  public static final String SLASH_BACKSLASH_PATTERN = "[/\\\\]";

  public static final String OR_OPERAND = " OR ";

  public static final String AND_OPERAND = " AND ";

  public static MultiMatchQueryBuilder multiMatchQueryBuilder(final String text,
      final String[] fields, final Map<String, Float> attributesBoost) {
    final MultiMatchQueryBuilder mmqb = QueryBuilders.multiMatchQuery(text, fields)
        .type(Type.CROSS_FIELDS)
        .operator(Operator.AND);

    if (!MapUtils.isEmpty(attributesBoost)) {
      Stream.of(fields).forEach(field -> mmqb.field(field,
          attributesBoost.getOrDefault(field, DF_BOOST)));
    }
    return mmqb;
  }

  public static QueryStringQueryBuilder queryStringBuilder(final String text,
      final String[] fields, final Map<String, Float> attributesBoost) {
    final QueryStringQueryBuilder strQueryBuilder = QueryBuilders.queryStringQuery(text)
        .defaultOperator(Operator.AND)
        .lenient(true);

    if (!MapUtils.isEmpty(attributesBoost)) {
      Stream.of(fields).forEach(field -> strQueryBuilder.field(field,
          attributesBoost.getOrDefault(field, DF_BOOST)));
    }
    return strQueryBuilder;
  }

  public static String removeNonAlphaChars(String input, boolean isChAlphaChars) {
    if (StringUtils.isBlank(input)) {
      return StringUtils.EMPTY;
    }
    return StringUtils.replacePattern(StringUtils.trim(input), getNonAlphaCharsPattern(isChAlphaChars),
        StringUtils.EMPTY);
  }

  public static String removeNonAlphaCharsExcluded(String input) {
    if (StringUtils.isBlank(input)) {
      return StringUtils.EMPTY;
    }
    return StringUtils.replacePattern(StringUtils.trim(input),
        getNonAlphaCharsPatternExcluded(useChAlphaCharactersByLangcode()), StringUtils.EMPTY);
  }

  private static String getNonAlphaCharsPatternExcluded(boolean isChAlphaChars) {
    return isChAlphaChars ? QueryUtils.NON_ALPHA_CHARS_EXCLUDED_PATTERN
        : QueryUtils.NON_ALPHA_CHARS_EXCLUDED_PATTERN_CS;
  }

  private static String getNonAlphaCharsPattern(boolean isChAlphaChars) {
    return isChAlphaChars ? NON_ALPHA_CHARS_PATTERN : NON_ALPHA_CHARS_PATTERN_CS_RO_SR_HU;
  }

  public static boolean useChAlphaCharactersByLangcode() {
    final String language = StringUtils.defaultString(LocaleContextHolder.getLocale().getLanguage(),
        Locale.GERMAN.getLanguage().toLowerCase());
    return getLangcodesForChPattern().contains(language);
  }

  private static List<String> getLangcodesForChPattern() {
    return Arrays.asList(LanguageCode.EN.getCode(),
        LanguageCode.DE.getCode(), LanguageCode.FR.getCode(), LanguageCode.IT.getCode());
  }

  public static String replaceEsSlashCharacter(String input) {
    return StringEscapeUtils.escapeJson(input);
  }

  public static String removeEsSpecialCharacter(String input) {
    return StringUtils.removePattern(input, "[/\"]");
  }

  public static String removeSpace(String input) {
    return StringUtils.removePattern(input, StringUtils.SPACE);
  }

  public static Collection<Function<String, String>> getEsSpecialCharacterParsers() {
    UnaryOperator<String> removeSpecialCharacterParser = QueryUtils::removeEsSpecialCharacter;
    UnaryOperator<String> esSlashCharacterParser = QueryUtils::replaceEsSlashCharacter;
    return Stream.of(removeSpecialCharacterParser, esSlashCharacterParser)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  public boolean hasWildCard(final String keyword) {
    return StringUtils.isNoneEmpty(keyword) && keyword.contains(SagConstants.WILDCARD);
  }
}
