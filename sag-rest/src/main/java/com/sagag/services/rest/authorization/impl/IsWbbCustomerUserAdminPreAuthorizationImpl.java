package com.sagag.services.rest.authorization.impl;

import com.sagag.eshop.service.dto.UserInfo;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class IsWbbCustomerUserAdminPreAuthorizationImpl extends AbstractAuthorization {

  @Override
  public String authorizeType() {
    return "isWbbCustomerUserAdmin";
  }

  @Override
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    if (authed == null || authed.getPrincipal() == null) {
      return false;
    }
    return ((UserInfo) authed.getPrincipal()).isWbbCustomerUserAdmin();
  }

}
