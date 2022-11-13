package com.sagag.services.incentive.authcookie;

import java.util.Map;

/**
 * Authorize cookie object.
 */
public class AuthCookie {

  private Map<CookieField, String> payload;
  private boolean valid;

  public AuthCookie(Map<CookieField, String> payload, boolean valid) {
    this.payload = payload;
    this.valid = valid;
  }

  public String getPayload(CookieField field) {
    return payload.get(field);
  }

  public boolean isValid() {
    return valid;
  }
}
