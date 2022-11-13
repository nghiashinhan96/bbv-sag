package com.sagag.services.oauth2.processor;

import com.sagag.services.common.enums.LoginMode;

public interface NonSaleAuthenticationProcessor extends UserAuthenticationProcessor {

  /**
   * Returns the login mode.
   *
   */
  LoginMode loginMode();
}
