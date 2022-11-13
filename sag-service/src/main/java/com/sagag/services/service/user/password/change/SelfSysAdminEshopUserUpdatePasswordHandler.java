package com.sagag.services.service.user.password.change;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.password.DefaultPasswordHashBuilder;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.SytemAdminChangePasswordDto;
import com.sagag.services.service.mail.SimpleChangePasswordCriteria;
import com.sagag.services.service.mail.SimpleChangePasswordMailSender;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@Slf4j
public class SelfSysAdminEshopUserUpdatePasswordHandler extends AbstractPasswordUserHandler
  implements UpdatePasswordHandler<SytemAdminChangePasswordDto> {

  @Autowired
  private SimpleChangePasswordMailSender simpleChangePasswordMailSender;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Autowired
  private DefaultPasswordHashBuilder defPasswordHashBuilder;

  /**
   * Updates password for system admin.
   *
   * @param user not required
   * @param model
   * @throws UserValidationException
   */
  @Override
  @Transactional
  public void updatePassword(UserInfo user, SytemAdminChangePasswordDto model)
    throws UserValidationException {
    log.debug("Updating system admin password");
    final Long userId = user.getId();
    Assert.notNull(userId, "The given user id must not be null");
    final EshopUser eshopUser = userService.getUserById(userId);
    final Login login = loginService.getLoginForUser(userId);
    if (!passwordFormatValidator.validate(model.getPassword())) {
      throw new UserValidationException(UserValidationException.UserErrorCase.UE_WPF_001,
        "Password wrong format");
    }

    login.setPasswordHash(
        defPasswordHashBuilder.buildPasswordHash(model.getPassword(), HashType.BCRYPT));
    loginService.update(login);

    // #5372: [CH/AT] Remove the plain-text password from the Back Office result mail
    // after changing the password
    final SimpleChangePasswordCriteria criteria = SimpleChangePasswordCriteria.builder()
        .toEmail(eshopUser.getEmail()).affiliateEmail(getSagDefaultEmail())
        .username(eshopUser.getUsername()).redirectUrl(model.getRedirectUrl())
        .locale(localeContextHelper.toLocale(eshopUser.getLangiso()))
        .rawPassword(StringUtils.EMPTY)
        .build();

    simpleChangePasswordMailSender.send(criteria);
  }
}
