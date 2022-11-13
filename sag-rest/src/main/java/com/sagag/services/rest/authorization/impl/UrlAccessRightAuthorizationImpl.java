package com.sagag.services.rest.authorization.impl;

import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.dto.UserInfo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation class for verifying access right URL permission.
 */
@Component
@Slf4j
public class UrlAccessRightAuthorizationImpl extends AbstractAuthorization {

  @Autowired
  private PermissionService permissionService;

  @Override
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    log.debug("Verifying url access right permission");
    final HttpServletRequest request = (HttpServletRequest) targetDomainObject;
    final UserInfo user = getUserInfo(authed);
    return this.permissionService.hasUrlPermission(user.getAllFunctions(), request.getServletPath());
  }

  @Override
  public String authorizeType() {
    return "isAccessibleUrl";
  }

}
