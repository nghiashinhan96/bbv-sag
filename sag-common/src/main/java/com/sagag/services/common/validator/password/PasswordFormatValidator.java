package com.sagag.services.common.validator.password;

import com.sagag.services.common.validator.IDataValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordFormatValidator implements IDataValidator<String> {

  // (?=.*\d)                 - Assert a string has at least one number
  // (?=.*[a-zA-Z])           - Assert a string has at least one number
  // [a-zA-Z0-9\\#$£\/! @\-.] - Assert that password only contains defined characters
  // {8,14}                   - Assert password is between 8 and 14 characters
  private static final String CREDENTIALS_PATTERN =
    "^(?=.*\\d)(?=.*[a-zA-Z])([a-zA-Z0-9\\\\#$£\\/! @\\-.]{8,14})$";

  /**
   * Validates password.
   *
   * @param password the password to validate
   * @return true if condition matched
   */
  @Override
  public boolean validate(String password) {
    return !StringUtils.isBlank(password) && password.matches(CREDENTIALS_PATTERN);
  }
}
