package com.sagag.eshop.service.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.exception.UserValidationException;

import java.util.Optional;

public interface IEshopUserFinder {

  /**
   * Returns the compatible user in Connect.
   *
   * @param input the user info to find
   * @param affiliate the affiliate to request find user.
   * @return the optional of {@link EshopUser}
   */
  Optional<EshopUser> findBy(String input, String affiliate) throws UserValidationException;

  boolean isMatchedFinder(String input, String affiliate);

  LoginInputType inputType();

}
