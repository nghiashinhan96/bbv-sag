package com.sagag.services.oauth2.processor.normal;

import com.sagag.services.common.enums.LoginMode;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.processor.NonSaleAuthenticationProcessor;

import org.springframework.stereotype.Component;

@Component
public class NormaUserAuthenticationProcessor implements NonSaleAuthenticationProcessor {

  @Override
  public EshopUserDetails process(EshopUserDetails details, Object... args) {
    return details;
  }

  @Override
  public LoginMode loginMode() {
    return LoginMode.NORMAL;
  }

}
