package com.sagag.services.service.user.password.change;

import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.validator.password.NewPasswordAndOldPasswordValidator;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@Slf4j
public class SelfEshopUserUpdatePasswordHandler extends AbstractPasswordUserHandler
  implements UpdatePasswordHandler<EshopUserLoginDto> {

  /**
   * Updates password for user by them own.
   *
   * @param user
   * @param eshopUserLoginDto
   * @throws UserValidationException
   */
  @Override
  @Transactional
  public void updatePassword(UserInfo user, EshopUserLoginDto eshopUserLoginDto)
    throws UserValidationException {
    eshopUserLoginDto.setId(user.getId());
    final Long userId = eshopUserLoginDto.getId();
    Assert.notNull(userId, "The given user id must not be null");
    final Login login = loginService.getLoginForUser(userId);

    if (!passwordFormatValidator.validate(eshopUserLoginDto.getPassword())) {
      throw new UserValidationException(UserValidationException.UserErrorCase.UE_WPF_001,
        "Password wrong format");
    }

    if (user.isAdmin()) {
      processChangePassword(user, eshopUserLoginDto, login);
      return;
    }

    final NewPasswordAndOldPasswordValidator.NewPasswordAndOldPasswordCriteria criteria =
      new NewPasswordAndOldPasswordValidator.NewPasswordAndOldPasswordCriteria(
        eshopUserLoginDto.getOldPassword(), login.getPasswordHash().getPassword());
    if (!newPasswordAndOldPasswordValidator.validate(criteria)) {
      log.warn("Security: Username {} input the wrong old password.");
      throw new UserValidationException(UserValidationException.UserErrorCase.UE_WOP_001,
        "Wrong old password");
    }
    processChangePassword(user, eshopUserLoginDto, login);
  }

  private void processChangePassword(UserInfo user, EshopUserLoginDto eshopUserLoginDto,
      Login login) {
    login.setPasswordHash(newPasswordHash(eshopUserLoginDto.getPassword(),
        user.isAdmin()));

    /*
     * note that it should do more check if new password is the same old db password before update.
     * not the scope of current ticket so I don't spend time on it.
     */
    // update password into DB
    loginService.update(login);
    final String affiliateEmail = getDefaultAffiliateEmail(eshopUserLoginDto, user.isAdmin(), user);
    changePasswordMailSender.send(buildSelfChangePasswordEmail(eshopUserLoginDto.getId(),
        affiliateEmail, eshopUserLoginDto.getRedirectUrl(), null, eshopUserLoginDto.isFinalUser()));
  }
}
