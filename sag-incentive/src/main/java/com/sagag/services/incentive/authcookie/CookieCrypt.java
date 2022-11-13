package com.sagag.services.incentive.authcookie;

import com.sagag.services.incentive.authcookie.CookiePrivacyException.PrivacyMode;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilities for CookieCrypt.
 */
@Slf4j
public class CookieCrypt {

  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  private static final String CIPHER = "AES/CBC/PKCS5Padding";

  private Key skeySpec;

  public CookieCrypt(String key) {
    try {
      byte[] raw;
      raw = key.getBytes(UTF_8);
      skeySpec = new SecretKeySpec(raw, "AES");
    } catch (UnsupportedEncodingException ex) {
      log.error("Error while initialize the CookieCrypt instance.", ex);
      throw new InstantiationError();
    }
  }

  public String encryptValueBase64(String value) throws CookiePrivacyException {
    byte[] encryptAES = encryptAES(value);
    String base64 = Base64.encodeBase64String(encryptAES);
    try {
      return URLEncoder.encode(base64, UTF_8);
    } catch (UnsupportedEncodingException ex) {
      throw new CookiePrivacyException(base64, PrivacyMode.ENCRYPT, ex);
    }
  }

  public String decryptValueBase64(String encryptedValue) throws CookiePrivacyException {
    try {
      String urlDecoded = URLDecoder.decode(encryptedValue, UTF_8);
      byte[] raw = Base64.decodeBase64(urlDecoded);
      return decryptAES(raw);
    } catch (Exception ex) {
      throw new CookiePrivacyException(encryptedValue, PrivacyMode.DECRYPT, ex);
    }
  }

  public byte[] encryptAES(String value) throws CookiePrivacyException {
    try {
      Cipher cipher = Cipher.getInstance(CIPHER);
      byte[] iv = new byte[cipher.getBlockSize()];

      AlgorithmParameterSpec ivParams = new IvParameterSpec(iv);
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParams);
      return cipher.doFinal(value.getBytes(UTF_8));
    } catch (Exception ex) {
      throw new CookiePrivacyException(value, PrivacyMode.ENCRYPT, ex);
    }
  }

  private String decryptAES(byte[] encrypted)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
      InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
      BadPaddingException {
    byte[] original;
    Cipher cipher;
    cipher = Cipher.getInstance(CIPHER);
    // the block size (in bytes), or 0 if the underlying algorithm is not a block cipher
    byte[] ivByte = new byte[cipher.getBlockSize()];
    // This class specifies an initialization vector (IV). Examples which use
    // IVs are ciphers in feedback mode, e.g., DES in CBC mode and RSA ciphers with OAEP encoding
    // operation.
    AlgorithmParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParamsSpec);
    original = cipher.doFinal(encrypted);
    return new String(original, UTF_8);
  }

  public static String createHashKey(final String alg, final String values) {
    try {
      MessageDigest md = MessageDigest.getInstance(alg);
      md.reset();
      byte[] raw = md.digest(values.getBytes(UTF_8));
      StringBuilder sb = new StringBuilder();
      for (int x : raw) {
        sb.append(Integer.toString((x & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      log.error("Algorithm not supported [hashfunction: {}, e:{}]", alg, e);
    } catch (UnsupportedEncodingException e) {
      log.error("Encoding not supported [hashfunction: UTF-8, e:{}]", e);
    }
    return StringUtils.EMPTY;
  }
}
