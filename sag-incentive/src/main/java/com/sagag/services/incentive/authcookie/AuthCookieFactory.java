package com.sagag.services.incentive.authcookie;

import static com.sagag.services.incentive.authcookie.CookieField.AP_KEY;
import static com.sagag.services.incentive.authcookie.CookieField.EXP_TIME_MS;
import static com.sagag.services.incentive.authcookie.CookieField.PW_HASH;
import static com.sagag.services.incentive.authcookie.CookieField.PW_HASH_TYPE;
import static com.sagag.services.incentive.authcookie.CookieField.USER_NAME;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.utils.NullUtils;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

/**
 * Utilities for AuthCookieFactory.
 */
@UtilityClass
@Slf4j
public final class AuthCookieFactory {

  private static final String LOGIN_INFO = "login_info";

  private static final Set<CookieField> HASHED_FIELDS =
      EnumSet.of(AP_KEY, USER_NAME, EXP_TIME_MS, PW_HASH, PW_HASH_TYPE);

  private static final String SECRET_FOR_HASH = "rFemuI2S*C20Ft>Jv5";

  private static final String AES_KEY = "]_gN6F:YI_I7^oqC";

  private static final String SEPARATOR = ",";
  private static final CookieCrypt COOKIE_CRYPT = new CookieCrypt(AES_KEY);
  private static final SecureRandom prng;

  public static Cookie createCookie(IncentiveLoginDto login, String accesspointKey,
      long timeoutMiliseconds, Date now) throws CookiePrivacyException {
    String cookie = createEncryptedTokenString(login, accesspointKey, timeoutMiliseconds, now);
    return buildCookie(cookie);
  }

  private static Cookie buildCookie(String cookie) {
    Cookie newCookie = new Cookie(LOGIN_INFO, cookie);
    newCookie.setMaxAge(-1);
    newCookie.setPath("/");
    newCookie.setSecure(true);
    return newCookie;
  }

  public static String createLoginToken(IncentiveLoginDto login, String accesspointKey,
      long timeoutMiliseconds) throws CookiePrivacyException {
    return createEncryptedTokenString(login, accesspointKey, timeoutMiliseconds,
        Calendar.getInstance().getTime());
  }

  private static String createEncryptedTokenString(IncentiveLoginDto login, String accesspointKey,
      long timeoutMiliseconds, Date now) throws CookiePrivacyException {
    Assert.notNull(login, "The given incentive login info must not be null");
    Assert.hasText(login.getUserName(), "The given username must not be empty");
    Assert.notNull(login.getType(), "The given password hash type");
    Assert.hasText(login.getPassword(), "The given password must not be empty");
    return COOKIE_CRYPT
        .encryptValueBase64(createTokenString(login, accesspointKey, timeoutMiliseconds, now));
  }

  private static String createTokenString(IncentiveLoginDto login, String accesspointKey,
      long timeoutMiliseconds, Date now) {
    long expireTimeMs = now.getTime() + timeoutMiliseconds;
    final String userName = login.getUserName();
    final String password = login.getPassword();
    final HashType hashType = login.getType();
    Map<CookieField, String> cookieMap =
        createCookieMap(accesspointKey, userName, password, hashType, expireTimeMs);
    return encodePayload(cookieMap);
  }

  private static Map<CookieField, String> createCookieMap(String accesspointKey, String userName,
      String password, HashType hashType, long expireTimeMs) {
    final Map<CookieField, String> cookieValues = new EnumMap<>(CookieField.class);
    cookieValues.put(USER_NAME, userName);
    cookieValues.put(AP_KEY, accesspointKey);
    cookieValues.put(PW_HASH, password);
    cookieValues.put(PW_HASH_TYPE, NullUtils.defaultEnum(hashType));
    cookieValues.put(EXP_TIME_MS, ((Long) expireTimeMs).toString());
    cookieValues.put(CookieField.COOKIE_HASH, generateHash(cookieValues));
    cookieValues.put(CookieField.SALT, ((Integer) prng.nextInt(10000)).toString());
    return cookieValues;
  }

  private static String encodePayload(Map<CookieField, String> cookieValues) {
    StringBuilder cookie = new StringBuilder();

    for (CookieField field : CookieField.values()) {
      if (cookie.length() > 0) {
        cookie.append(SEPARATOR);
      }
      cookie.append(cookieValues.get(field));
    }
    return cookie.toString();
  }

  private static String generateHash(Map<CookieField, String> fields) {
    StringBuilder builder = new StringBuilder();
    for (CookieField field : CookieField.values()) {
      if (HASHED_FIELDS.contains(field)) {
        builder.append(fields.get(field));
      }
    }
    builder.append(SECRET_FOR_HASH);
    return DigestUtils.sha1Hex(builder.toString());
  }

  static {
    try {
      prng = SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException e) {
      String msg = "initializing SecureRandom failed";
      log.error(msg, e);
      throw new IllegalArgumentException(msg);
    }
  }


  public static AuthCookie decodeAndValidateLoginToken(String loginToken) {
    log.trace("decodeAndValidate");
    try {
      String value = COOKIE_CRYPT.decryptValueBase64(loginToken);
      Map<CookieField, String> cookieMap = decodePayload(value);

      String generatedHash = generateHash(cookieMap);
      long timestampFromCookie = Long.parseLong(cookieMap.get(EXP_TIME_MS));
      long diff = timestampFromCookie - System.currentTimeMillis();
      if ((diff > 0) && generatedHash.equals(cookieMap.get(CookieField.COOKIE_HASH))) {
        return new AuthCookie(cookieMap, true);
      }
      return new AuthCookie(null, false);
    } catch (NumberFormatException | CookiePrivacyException | ArrayIndexOutOfBoundsException ex) {
      log.debug("validateCookie", ex);
      return new AuthCookie(null, false);
    }
  }

  public static AuthCookie decodeAndValidateLoginToken(String loginToken, String accesspointKey,
      Date now) {
    log.trace("decodeAndValidate");
    try {
      String value = COOKIE_CRYPT.decryptValueBase64(loginToken);
      Map<CookieField, String> cookieMap = decodePayload(value);

      String generatedHash = generateHash(cookieMap);
      long timestampFromCookie = Long.parseLong(cookieMap.get(EXP_TIME_MS));
      long diff = timestampFromCookie - now.getTime();
      if ((diff > 0) && generatedHash.equals(cookieMap.get(CookieField.COOKIE_HASH))
          && accesspointKey.equals(cookieMap.get(AP_KEY))) {
        return new AuthCookie(cookieMap, true);
      }
      return new AuthCookie(null, false);
    } catch (NumberFormatException | CookiePrivacyException | ArrayIndexOutOfBoundsException ex) {
      log.debug("validateCookie", ex);
      return new AuthCookie(null, false);
    }
  }


  private static Map<CookieField, String> decodePayload(String payload) {
    String[] decodedValue = payload.split(SEPARATOR);
    Map<CookieField, String> cookieValues = new EnumMap<>(CookieField.class);
    for (CookieField field : CookieField.values()) {
      cookieValues.put(field, decodedValue[field.ordinal()]);
    }
    return cookieValues;
  }


}
