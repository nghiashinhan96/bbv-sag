package com.sagag.services.oauth2.processor.dms;

import com.sagag.services.common.enums.LoginMode;

import org.springframework.stereotype.Component;

@Component
public class DmsUserAuthenticationProcessor extends AbstractDmsUserAuthenticationProcessor {

  @Override
  public LoginMode loginMode() {
    return LoginMode.DMS;
  }

}
