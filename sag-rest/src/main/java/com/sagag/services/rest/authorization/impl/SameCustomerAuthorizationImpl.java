package com.sagag.services.rest.authorization.impl;

import com.sagag.eshop.repo.api.VUserDetailRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation class for verifying same customer permission.
 */
@Component
@Slf4j
public class SameCustomerAuthorizationImpl extends AbstractAuthorization {

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Override
  protected boolean hasPermission(Authentication authed, Object targetDomainObject) {
    log.debug("Verifying same customer permission");
    final Long userId = (Long) targetDomainObject;
    Optional<String> modifiedUserOrgCode = vUserDetailRepo.findOrgCodeByUserId(userId);
    Optional<String> currentUserOrgCode =
        vUserDetailRepo.findOrgCodeByUserId(getUserInfo(authed).getId());
    return modifiedUserOrgCode.isPresent()
        && currentUserOrgCode.isPresent()
        && modifiedUserOrgCode.get().equals(currentUserOrgCode.get());
  }

  @Override
  public String authorizeType() {
    return "isTheSameCustomer";
  }

}
