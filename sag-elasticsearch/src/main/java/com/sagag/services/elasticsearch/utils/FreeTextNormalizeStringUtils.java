package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.contants.SagConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class FreeTextNormalizeStringUtils {

  private static final String OR_OPERAND = QueryUtils.OR_OPERAND;

  private static final String AND_OPERAND = QueryUtils.AND_OPERAND;

  private static final String OPEN_BRACKET = SagConstants.OPEN_BRACKET;

  private static final String CLOSE_BRACKET = SagConstants.CLOSE_BRACKET;

  private static final Pattern START_WITH_DIGIT_PATTERN = Pattern.compile("(^[0-9]+).*");

  private static final char WILDCARD = ElasticsearchConstants.WILDCARD;

  private static final String EXACT_NON_ALPHA_CHARS_PATTERN = "[^a-zA-Z0-9]";

  private static final String LEADING_ZERO = "0";

  public static String handleExcludedChar(final String text) {
    final String textStripZero = stripLeadingZerosOrEmpty(text);
    final boolean useChAlphaCharacters = QueryUtils.useChAlphaCharactersByLangcode();
    final String normalized =
        normalizeFreetext(QueryUtils.removeNonAlphaChars(text, useChAlphaCharacters));
    final String textUpperCase = StringUtils.upperCase(text);
    if (StringUtils.isBlank(textStripZero)) {
      return StringUtils.containsAny(text, QueryUtils.EXCLUDED_CHAR)
          ? or(normalized, text, textUpperCase)
          : or(normalized, textUpperCase);
    }
    final String normalizedStripZeros =
        normalizeFreetext(QueryUtils.removeNonAlphaChars(textStripZero, useChAlphaCharacters));
    return StringUtils.containsAny(text, QueryUtils.EXCLUDED_CHAR)
        ? or(normalized, normalizedStripZeros, text, textStripZero, textUpperCase)
        : or(normalized, normalizedStripZeros, textUpperCase);
  }

  public static String buildPerfectMatchCondition(final String text) {
    return buildPerfectMatchCondition(text, true);
  }

  public static String buildPerfectMatchConditionWithoutStripZero(final String text) {
    return buildPerfectMatchCondition(text, false);
  }

  public static String buildPerfectMatchCondition(final String text, final boolean isStripZero) {
    if (StringUtils.isBlank(text)) {
      return StringUtils.EMPTY;
    }

    final String textStripZero;
    if (isStripZero) {
      textStripZero = stripLeadingZero(text);
    } else {
      textStripZero = text;
    }

    return StringUtils.replacePattern(StringUtils.trim(textStripZero),
      EXACT_NON_ALPHA_CHARS_PATTERN, StringUtils.EMPTY);
  }

  public static String stripLeadingZero(final String text) {
    if (StringUtils.isBlank(text)) {
      return StringUtils.EMPTY;
    }
    return StringUtils.stripStart(text, LEADING_ZERO);
  }

  public static String normalizeFreetext(final String text) {
    if (StringUtils.contains(text, SagConstants.WILDCARD)) { // #1668, only search wildcard
      return text;
    }
    final String[] terms = StringUtils.split(text, StringUtils.SPACE);
    final List<String> normalizeTerms = new ArrayList<>();
    for (final String term : terms) {
      final Matcher matcher = START_WITH_DIGIT_PATTERN.matcher(term);
      if (matcher.find() && !StringUtils.isNumeric(term)) {
        normalizeTerms.add(createOrCombiningTerms(matcher, term));
      } else {
        normalizeTerms.add(term);
      }
    }
    final int termLength = terms.length;
    String termQuery = StringUtils.join(normalizeTerms, AND_OPERAND);
    if (termLength > 2) {
      // combine the first and the second word
      termQuery = or(termQuery, StringUtils.join(terms[0], terms[1]));
    }
    if (termLength > 1) {
      termQuery = or(termQuery, StringUtils.join(terms));
    }
    return termQuery;
  }

  public static String stripLeadingZerosOrEmpty(String input) {
    final String textStriped = stripLeadingZero(input);
    return StringUtils.length(textStriped.trim()) < StringUtils.length(input.trim()) ? textStriped
        : StringUtils.EMPTY;
  }

  private static String createOrCombiningTerms(final Matcher matcher, final String term) {
    final List<String> combiningOrQuery = new ArrayList<>();
    combiningOrQuery.add(term);
    final String digits = matcher.group(1);
    final String remaining = StringUtils.substring(term, matcher.end(1));
    combiningOrQuery.add(new StringBuilder(OPEN_BRACKET).append(digits).append(AND_OPERAND)
        .append(remaining).append(CLOSE_BRACKET).toString());
    return new StringBuilder(OPEN_BRACKET)
        .append(StringUtils.join(combiningOrQuery.toArray(new String[] {}), OR_OPERAND))
        .append(CLOSE_BRACKET).toString();
  }

  private static String or(final String left, final String right) {
    if (StringUtils.equals(left, right)) {
      return left;
    }
    return new StringBuilder(OPEN_BRACKET).append(left).append(CLOSE_BRACKET).append(OR_OPERAND)
        .append(OPEN_BRACKET).append(right).append(CLOSE_BRACKET).toString();
  }

  public String or(String... strs) {
    if (ArrayUtils.isEmpty(strs)) {
      return StringUtils.EMPTY;
    }
    List<String> strList =
        Stream.of(strs).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
    if (strList.size() == 1) {
      return strList.stream().findFirst().orElse(StringUtils.EMPTY);
    }
    return strList.stream().map(str -> OPEN_BRACKET + str + CLOSE_BRACKET)
        .collect(Collectors.joining(OR_OPERAND));
  }

  public String and(String... strs) {
    if (ArrayUtils.isEmpty(strs)) {
      return StringUtils.EMPTY;
    }
    return Stream.of(strs).filter(StringUtils::isNotBlank)
        .map(str -> OPEN_BRACKET + str + CLOSE_BRACKET).collect(Collectors.joining(AND_OPERAND));
  }

  public static String normalizeVehicleFullName(final String vehicleFullName) {
    if (StringUtils.isBlank(vehicleFullName)) {
      return StringUtils.EMPTY;
    }
    if (StringUtils.containsAny(vehicleFullName, WILDCARD)) {
      return StringUtils.trim(vehicleFullName);
    }
    return analyzeVehFullName(StringUtils.trim(vehicleFullName));
  }

  private static String analyzeVehFullName(String vehFullName) {
    final Function<String, String> vehFullNamAnalyzer;
    if (StringUtils.containsAny(vehFullName, StringUtils.SPACE)) {
      vehFullNamAnalyzer = value -> {
        final String[] strs =
            new String[] { value, Stream.of(StringUtils.split(value, StringUtils.SPACE))
                .map(item -> WILDCARD + item + WILDCARD).collect(Collectors.joining(AND_OPERAND)) };
        return or(strs);
      };
    } else {
      vehFullNamAnalyzer =
          value -> or(value, value + WILDCARD, WILDCARD + value, WILDCARD + value + WILDCARD);
    }
    return Optional.of(vehFullName).map(vehFullNamAnalyzer).orElse(StringUtils.EMPTY);
  }

  public static String replaceNonAlphaChars(String replace, String replaceBy) {
    return StringUtils.replacePattern(replace, EXACT_NON_ALPHA_CHARS_PATTERN, replaceBy);
  }
}
