package com.sagag.services.service.user.password.change;

import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
public class OnbehalfEshopUserUpdatePasswordHandler extends AbstractPasswordUserHandler
  implements UpdatePasswordHandler<EshopUserLoginDto> {

  private static final boolean DISABLE_UPDATE_FOR_SYS_ADMIN = false;

  /**
   * Updates password for user by customer administrator.
   *
   * @param user
   * @param eshopUserLoginDto
   * @throws UserValidationException
   */
  @Override
  @Transactional
  public void updatePassword(UserInfo user, EshopUserLoginDto eshopUserLoginDto)
    throws UserValidationException {
    final Long userId = eshopUserLoginDto.getId();
    Assert.notNull(userId, "The given user id must not be null");
    final String password = eshopUserLoginDto.getPassword();
    if (!passwordFormatValidator.validate(password)) {
      throw new UserValidationException(UserValidationException.UserErrorCase.UE_WPF_001,
        "Password wrong format");
    }

    final Login login = loginService.getLoginForUser(userId);
    Optional.of(newPasswordHash(password, DISABLE_UPDATE_FOR_SYS_ADMIN))
    .ifPresent(login::setPasswordHash);

    // Update password into DB
    loginService.update(login);

    final String affiliateEmail = getDefaultAffiliateEmail(eshopUserLoginDto,
        DISABLE_UPDATE_FOR_SYS_ADMIN, user);
    changePasswordMailSender.send(buildOnbehalfChangePasswordEmail(userId, affiliateEmail,
      eshopUserLoginDto.getRedirectUrl(), null, eshopUserLoginDto.isFinalUser()));
  }
}
