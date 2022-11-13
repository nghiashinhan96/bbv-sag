package com.sagag.services.service.user.password;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.service.validator.EshopUserFromAxServiceValidator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChangePasswordPermissionEvaluator {

  @Autowired
  private EshopUserFromAxServiceValidator eshopUserFromAxServiceValidator;

  public void check(EshopUser user, String affiliateId) throws UserValidationException {
    try {
      // Check authority, check sale assistant authority
      if (!user.isGroupAdminRole() && !user.isUserAdminRole() && !user.isNormalUserRole()
          && !user.isSalesAssistantRole()) {
        final String message = "You are not authorized to use this page";
        log.error(message);
        throw new AccessDeniedException(message);
      }

      // Check user exists in ERP System
      if (!eshopUserFromAxServiceValidator.validate(user, affiliateId)) {
        throw new IllegalArgumentException();
      }

    } catch (IllegalArgumentException | AccessDeniedException ex) {
      final String message = "You are not authorized to use this page";
      throw new UserValidationException(UserErrorCase.UE_NAU_001, message);
    }
  }
}
