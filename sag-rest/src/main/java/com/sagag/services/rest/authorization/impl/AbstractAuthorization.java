package com.sagag.services.rest.authorization.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.rest.authorization.IAuthorize;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract Authorization.
 */
@Slf4j
public abstract class AbstractAuthorization implements IAuthorize {

  protected abstract boolean hasPermission(Authentication authed, Object targetDomainObject);

  @Override
  public boolean authorize(Authentication authed, Object targetDomainObject) {
    logAuthorizationInfo(authed);
    return hasPermission(authed, targetDomainObject);
  }

  private void logAuthorizationInfo(Authentication authed) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    final UserInfo user = getUserInfo(authed);
    log.debug("User={} is trying to access url={}", user.getUsername(), request.getServletPath());
  }

  protected UserInfo getUserInfo(Authentication authed) {
    return (UserInfo) authed.getPrincipal();
  }

}
