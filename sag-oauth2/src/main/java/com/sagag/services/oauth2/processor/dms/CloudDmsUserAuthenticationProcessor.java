package com.sagag.services.oauth2.processor.dms;

import com.sagag.services.common.enums.LoginMode;

import org.springframework.stereotype.Component;

@Component
public class CloudDmsUserAuthenticationProcessor extends AbstractDmsUserAuthenticationProcessor {

  @Override
  public LoginMode loginMode() {
    return LoginMode.CLOUD_DMS;
  }

}
