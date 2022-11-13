package com.sagag.services.common.validator.password;

import com.sagag.services.common.validator.IDataValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class NewPasswordAndOldPasswordValidator
  implements IDataValidator<NewPasswordAndOldPasswordValidator.NewPasswordAndOldPasswordCriteria> {

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Verifies if valid old password when user change password in profile
   *
   * @param criteria the validation criteria
   * @return true if valid, otherwise
   */
  @Override
  public boolean validate(NewPasswordAndOldPasswordCriteria criteria) {
    /*
     * checking the old password in database
     */
    return passwordEncoder.matches(criteria.getOldPassword(), criteria.getNewPassword());
  }

  @Getter
  @RequiredArgsConstructor
  public static class NewPasswordAndOldPasswordCriteria {
    private final  String oldPassword;
    private final String newPassword;
  }
}
