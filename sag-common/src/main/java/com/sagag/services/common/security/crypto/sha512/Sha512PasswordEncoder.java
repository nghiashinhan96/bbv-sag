package com.sagag.services.common.security.crypto.sha512;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.security.crypto.BasePasswordEncoder;
import com.sagag.services.common.utils.HashUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;

@Component
@Slf4j
public class Sha512PasswordEncoder implements BasePasswordEncoder {

  private static final String HEX_STRING_PREFIX = "0x";

  @Override
  public String encode(CharSequence rawPassword) {
    throw new UnsupportedOperationException("Unsupported encode with SHA-512 algorithm.");
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPasswordAndSalt) {
    Assert.notNull(encodedPasswordAndSalt, "The given encoded password and salt must not be empty");
    if (!HashUtils.isSha512(encodedPasswordAndSalt)) {
      return false;
    }

    final String[] encodedPasswordAndSaltArray =
        HashUtils.extractEncodedPasswordAndSalt(encodedPasswordAndSalt);
    final String hexPassword = encodedPasswordAndSaltArray[0];
    final String hexSalt = encodedPasswordAndSaltArray[1];
    if (!StringUtils.startsWithIgnoreCase(hexPassword, HEX_STRING_PREFIX)) {
      return false;
    }
    final String hexPasswordWithoutPrefix =
        StringUtils.removeStartIgnoreCase(hexPassword, HEX_STRING_PREFIX);
    final String hexSaltWithoutPrefix =
        StringUtils.removeStartIgnoreCase(hexSalt, HEX_STRING_PREFIX);
    try {
      byte[] saltBytes = Hex.decodeHex(hexSaltWithoutPrefix);
      final Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_512, saltBytes);
      mac.update(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
      final String encodePassword = String.valueOf(Hex.encodeHex(mac.doFinal()));
      return StringUtils.equalsIgnoreCase(hexPasswordWithoutPrefix, encodePassword);
    } catch (DecoderException e) {
      log.error("Failed to decode SHA512 salt", e);
    }
    return false;
  }

  @Override
  public HashType hashType() {
    return HashType.SHA_512;
  }

}
