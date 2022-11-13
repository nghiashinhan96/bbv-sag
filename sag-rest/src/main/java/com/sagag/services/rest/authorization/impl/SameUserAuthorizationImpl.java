package com.sagag.services.rest.authorization.impl;

import com.sagag.eshop.service.dto.UserInfo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation class for verifying same user permission.
 */
@Component
@Slf4j
public class SameUserAuthorizationImpl extends AbstractAuthorization {

  @Override
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    log.debug("Verifying same user permission");
    final UserInfo user = getUserInfo(authed);
    return Objects.isNull(targetDomainObject) || ((Long) targetDomainObject).equals(user.getId());
  }

  @Override
  public String authorizeType() {
    return "isTheSameUser";
  }
}
