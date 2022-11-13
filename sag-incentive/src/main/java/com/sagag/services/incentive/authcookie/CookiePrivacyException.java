package com.sagag.services.incentive.authcookie;

import com.sagag.services.common.exception.ServiceException;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception to handle encrypt or decrypt exception.
 */
@EqualsAndHashCode(callSuper = true)
public class CookiePrivacyException extends ServiceException {

  private static final long serialVersionUID = 5469652146347686584L;

  enum PrivacyMode {
    ENCRYPT, // encrypt mode
    DECRYPT // decrypt mode
  }

  private final String data;
  private final PrivacyMode mode;

  public CookiePrivacyException(String data, PrivacyMode mode, Throwable throwable) {
    super(buildErrorMessage(data, mode), throwable);
    this.data = data;
    this.mode = mode;
    setMoreInfos(buildMoreInfos());
  }

  private static String buildErrorMessage(String data, PrivacyMode mode) {
    if (PrivacyMode.ENCRYPT == mode) {
      return String.format("Error while encrypting the value = %s", data);
    }
    return String.format("Error while decrypting the value = %s", data);
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    final Map<String, Object> moreInfos = new HashMap<>();
    moreInfos.put("INFO_KEY_DATA", data);
    moreInfos.put("INFO_KEY_MODE", mode);
    return moreInfos;
  }
}
