package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.user.LoginInputType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmailEshopUserFinderImpl extends AbstractEshopUserFinder {

  private static final String MULTIPLE_EMAIL_ADDRESSES_ERROR_MESSAGE =
      "There are more than one accounts link to this e-maill address. "
          + "Please enter the username you would like to reset password.";

  /**
   * The functional implementation for finding user by email.
   *
   */
  @Override
  public Optional<EshopUser> findBy(String email, String affiliate) throws UserValidationException {
    final List<EshopUser> users = userService.getUsersByEmail(email);
    if (CollectionUtils.isEmpty(users)) {
      final String errorMsg = String.format("Not found user by email = %s", email);
      throw new UserValidationException(UserErrorCase.UE_NFU_001, errorMsg);
    }
    if (CollectionUtils.size(users) > 1) {
      throw new UserValidationException(UserErrorCase.UE_MEA_001,
          MULTIPLE_EMAIL_ADDRESSES_ERROR_MESSAGE);
    }
    return users.stream().findFirst();
  }

  @Override
  public boolean isMatchedFinder(String input, String affiliate) {
    return !StringUtils.isBlank(input) && emailValidator.isValid(input, null);
  }

  @Override
  public LoginInputType inputType() {
    return LoginInputType.EMAIL;
  }

}
