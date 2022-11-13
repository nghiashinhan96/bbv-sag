package com.sagag.services.tools.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

/**
 * Class to provide some utility features in DVSE user context.
 *
 */
@UtilityClass
public final class DvseUserUtils {

  public static final int MAX_UNIQUE_NAME_RETRY = 5;

  public static final String generateRandomCustomerName() {
    return randomString(5);
  }

  public static final String generateRandomUsername() {
    return randomString(8);
  }

  public static final String generateRandomPassword() {
    return randomString(8);
  }

  public static String randomString(int bytes) {
    SecureRandom random = new SecureRandom();
    byte ary[] = new byte[bytes];
    random.nextBytes(ary);

    return DatatypeConverter.printHexBinary(ary);
  }

}
