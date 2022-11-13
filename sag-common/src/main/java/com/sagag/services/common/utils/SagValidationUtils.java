package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Utilities for validation.
 */
@UtilityClass
public final class SagValidationUtils {

  /**
   * Returns the hibernate object validator from factory.
   */
  public static Validator getObjectValidator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }

  /**
   * Returns the violations from objects.
   *
   * @param object the main validation object
   * @param groups the additional objects.
   * @return the set of {@link ConstraintViolation} of generic object
   */
  public static <T> Set<ConstraintViolation<T>> validateObject(T object, Class<?>... groups) {
    return getObjectValidator().validate(object, groups);
  }

  /**
   * Returns the first violation from objects.
   *
   * @param object the main validation object
   * @param groups the additional objects.
   * @return the set of {@link ConstraintViolation} of generic object
   */
  public static <T> Optional<ConstraintViolation<T>> validateObjectAndReturnFirstError(T object,
      Class<?>... groups) {
    return validateObject(object, groups).stream().findFirst();
  }
}
