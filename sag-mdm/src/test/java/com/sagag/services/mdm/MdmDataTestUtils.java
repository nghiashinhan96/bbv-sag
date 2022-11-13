package com.sagag.services.mdm;

import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

import lombok.experimental.UtilityClass;

/**
 * Unit test util class.
 */
@UtilityClass
public class MdmDataTestUtils {

  public static String randomString(int bytes) {
    SecureRandom random = new SecureRandom();
    byte ary[] = new byte[bytes];
    random.nextBytes(ary);

    return DatatypeConverter.printHexBinary(ary);
  }

}
