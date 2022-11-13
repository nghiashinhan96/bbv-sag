package com.sagag.services.service.utils;

import com.sagag.services.elasticsearch.criteria.Telephone;
import com.sagag.services.service.exception.TelephoneFormatException;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils class to extract the telephone number and its country code.
 */
public final class TelephoneUtils {

  private static final String ZERO = "0";
  private static final String TELEPHONE_PATTERN = "^(0{1,2}|\\+43|\\+41).*";
  private static final Pattern COMPILED_PATTERN = Pattern.compile(TELEPHONE_PATTERN);
  private static final String[] ALLOWED_CC = {"41", "43"};

  private TelephoneUtils() {
    // intentionally blank
  }

  /**
   * Extracts the telephone with the respective number and its country code.
   *
   * @param telephone the telephone to extract
   * @return the {@link Telephone} instance with respective number and its country code.
   * @throws TelephoneFormatException thrown when program fails.
   */
  public static Optional<Telephone> extract(final String telephone) throws TelephoneFormatException {
    final Matcher matcher = COMPILED_PATTERN.matcher(telephone);
    if (!matcher.matches()) {
      throw new TelephoneFormatException(telephone);
    }
    final String first = matcher.group(1);
    final String remaining = StringUtils.substring(telephone, matcher.end(1));
    if (StringUtils.startsWith(first, "+")) {
      return Optional.of(
          new Telephone(StringUtils.substring(first, 1), removeUnexpectedChars(remaining)));
    }
    if (StringUtils.startsWith(first, "00")) {
      final String countryCode = StringUtils.substring(telephone, 2, 4); // take 2 next digits
      if (!Arrays.asList(ALLOWED_CC).contains(countryCode)) {
        throw new TelephoneFormatException(telephone);
      }
      return Optional.of(
          new Telephone(countryCode, removeUnexpectedChars(StringUtils.substring(telephone, 4))));
    }
    if (StringUtils.startsWith(first, ZERO)) {
      // uses default country code
      return Optional.of(
          new Telephone(StringUtils.EMPTY, removeUnexpectedChars(remaining)));
    }
    throw new TelephoneFormatException(telephone);
  }

  private static String removeUnexpectedChars(final String remaining) {
    // remove all ()
    String afterRemovingVal = remaining.replaceAll("\\p{P}", StringUtils.EMPTY);
    // remove all /
    afterRemovingVal = StringUtils.replace(afterRemovingVal, "/", StringUtils.EMPTY);
    // remove all spaces
    afterRemovingVal = afterRemovingVal.replaceAll("\\s+", StringUtils.EMPTY);
    // remove all leading single zero
    return StringUtils.stripStart(afterRemovingVal, ZERO);
  }


  /**
   * Checks if the telephone is valid or not.
   *
   * <p>
   * the telephone should start with 0 or 00 or +41 or +43
   *
   * @param telephone the telephone entered by user.
   * @return <code>true</code> if the telephone is valid, otherwise <code>false</code>
   */
  public static boolean matchesRegex(final String telephone) {
    final Matcher matcher = COMPILED_PATTERN.matcher(telephone);
    return matcher.matches();
  }
}
