package com.sagag.services.oauth2.processor;

import com.sagag.services.common.enums.LoginMode;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.processor.normal.NormaUserAuthenticationProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Primary
public class CompositeUserAuthenticationProcessor implements NonSaleAuthenticationProcessor {

  @Autowired
  private List<NonSaleAuthenticationProcessor> authenticationProcessors;

  @Autowired
  private NormaUserAuthenticationProcessor defaultNormalUserAuthProcessor;

  @Autowired
  private EshopAuthHelper eshopAuthenticationHelper;

  @Override
  public EshopUserDetails process(EshopUserDetails details, Object... args) {
    final Optional<NonSaleAuthenticationProcessor> processorOpt = authenticationProcessors.stream()
        .filter(processor -> processor.loginMode() == eshopAuthenticationHelper.getLoginMode())
        .findFirst();
    return processorOpt.orElse(defaultNormalUserAuthProcessor).process(details, args);
  }

  @Override
  public LoginMode loginMode() {
    throw new UnsupportedOperationException("Unsupported function for this implementation");
  }

}
