package com.sagag.services.rest.authorization.impl;

import com.sagag.services.common.aspect.LogExecutionTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class HasWholesalerAuthorizationImpl extends AbstractAuthorization {

  @Override
  @LogExecutionTime
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    return getUserInfo(authed).hasWholesalerPermission();
  }

  @Override
  public String authorizeType() {
    return "hasWholesalerPermission";
  }

}
