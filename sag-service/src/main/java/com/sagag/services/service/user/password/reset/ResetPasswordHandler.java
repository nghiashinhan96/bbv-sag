package com.sagag.services.service.user.password.reset;

import com.sagag.services.common.exception.ServiceException;

public interface ResetPasswordHandler<T, R> {

  /**
   * Handles the reset password steps.
   *
   * @param criteria the object criteria
   * @return the result object
   */
  R handle(T criteria) throws ServiceException;
}
