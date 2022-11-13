package com.sagag.services.common.utils;

import com.google.common.collect.Lists;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
@Slf4j
public class HashUtils {

  private static final String SHA_1_TYPE = "SHA-1";

  private static final String MD5_TYPE = "MD5";

  public static final String SHA_512 = "HmacSHA512";

  public static final String SHA_512_DELIMITER = "_{" + SHA_512 +"}_";

  public String hashMD5(String value) {
    return hashByFunction(MD5_TYPE, value);
  }

  public byte[] hashMD5ReturnRawBinary(String value) {
    return hashByFunctionWithRawData(MD5_TYPE, value);
  }

  public String hashSHA1(String value) {
    return hashByFunction(SHA_1_TYPE, value);
  }

  private String hashByFunction(final String hashFunction, final String keyValues) {
    if (StringUtils.isBlank(hashFunction) || StringUtils.isBlank(keyValues)) {
      return StringUtils.EMPTY;
    }
    try {
      MessageDigest md = MessageDigest.getInstance(hashFunction);
      md.reset();
      byte[] raw = md.digest(keyValues.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder(StringUtils.EMPTY);
      for (int x : raw) {
        sb.append(Integer.toString((x & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      log.error("Algorithm not supported [hashfunction: {}, e:{}]", hashFunction, e);
      return StringUtils.EMPTY;
    }
  }

  private byte[] hashByFunctionWithRawData(final String hashFunction, final String keyValues) {
    byte[] emptyArray = new byte[0];
    if (StringUtils.isBlank(hashFunction) || StringUtils.isBlank(keyValues)) {
      return emptyArray;
    }
    try {
      MessageDigest md = MessageDigest.getInstance(hashFunction);
      md.reset();
      return md.digest(keyValues.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      log.error("Algorithm not supported [hashfunction: {}, e:{}]", hashFunction, e);
      return emptyArray;
    }
  }

  /**
   * Returns the password and salt as string.
   *
   * @param password
   * @param salt
   * @return the encoded password and salt
   */
  public String getEncodedPasswordAndSaltAsString(String password, String salt) {
    return StringUtils.join(Lists.newArrayList(password, salt), SHA_512_DELIMITER);
  }

  public String[] extractEncodedPasswordAndSalt(String encodedPasswordAndSalt) {
    return StringUtils.splitByWholeSeparator(encodedPasswordAndSalt, SHA_512_DELIMITER);
  }

  public static boolean isSha512(String encodedPassword) {
    return StringUtils.containsIgnoreCase(encodedPassword, SHA_512_DELIMITER);
  }
}
