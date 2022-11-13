package com.sagag.services.service.user.password.change;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;

public interface UpdatePasswordHandler<T> {

  /**
   * Update user password.
   *
   * @param user the current user context
   * @param criteria the password update criteria
   * @throws UserValidationException
   */
  void updatePassword(UserInfo user, T criteria) throws UserValidationException;
}
