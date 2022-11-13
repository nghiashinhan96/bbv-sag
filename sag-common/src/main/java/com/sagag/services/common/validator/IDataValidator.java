package com.sagag.services.common.validator;

import com.sagag.services.common.exception.ValidationException;

/**
 * Interface to execute complex business validation.
 *
 * @param <T> the generic object of criteria.
 */
@FunctionalInterface
public interface IDataValidator<T> {

  /**
   * Validates the generic criteria.
   *
   * @param criteria the generic criteria to validation
   * @return {@code true} if the input criteria is valid, otherwise {@code false}
   * @throws ValidationException the exception when the data validation fails
   */
  boolean validate(T criteria) throws ValidationException;
}
