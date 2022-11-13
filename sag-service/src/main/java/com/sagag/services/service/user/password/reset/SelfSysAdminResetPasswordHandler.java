package com.sagag.services.service.user.password.reset;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.password.DefaultPasswordHashBuilder;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.SytemAdminResetPasswordDto;
import com.sagag.services.service.mail.SimpleChangePasswordCriteria;
import com.sagag.services.service.mail.SimpleChangePasswordMailSender;
import com.sagag.services.service.user.password.change.AbstractPasswordUserHandler;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class SelfSysAdminResetPasswordHandler extends AbstractPasswordUserHandler
  implements ResetPasswordHandler<SytemAdminResetPasswordDto, Void> {

  private static final String INVALID_EMAIL = "Invalid username e-mail";

  private static final String NOT_AUTHORIZED = "You are not authorized to use this function";

  @Autowired
  private SimpleChangePasswordMailSender simpleChangePasswordMailSender;

  @Autowired
  private UserSearchFactory userSearchFactory;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Autowired
  private DefaultPasswordHashBuilder defPasswordHashBuilder;

  /**
   * Resets password for system admin.
   *
   * @param resetPasswordUserDto the data need for the process
   * @throws UserValidationException
   */
  @Override
  @Transactional
  public Void handle(SytemAdminResetPasswordDto resetPasswordUserDto)
    throws UserValidationException {
    log.debug("reset system admin password");
    final String email = resetPasswordUserDto.getEmail();
    if (StringUtils.isBlank(email)) {
      throw new UserValidationException(UserValidationException.UserErrorCase.UE_IEM_001,
        INVALID_EMAIL);
    }

    final EshopUser user = userSearchFactory.searchEshopUserByInput(email, SAG)
      .orElseThrow(() -> new UserValidationException(UserValidationException.UserErrorCase.UE_NFE_001,
        INVALID_EMAIL));
    if (!user.isAdmin()) {
      throw new UserValidationException(UserValidationException.UserErrorCase.UE_NAU_001,
        NOT_AUTHORIZED);
    }

    final String genPassword =
      RandomStringUtils.randomAlphanumeric(SagConstants.DEFAULT_MIN_LENGTH_PASSWORD);

    final Login login = user.getLogin();
    login.setPasswordHash(defPasswordHashBuilder.buildPasswordHash(genPassword, HashType.BCRYPT));
    loginService.update(login);

    //@formatter:off
    final SimpleChangePasswordCriteria criteria =
      SimpleChangePasswordCriteria.builder()
        .username(user.getUsername())
        .toEmail(user.getEmail())
        .affiliateEmail(getSagDefaultEmail())
        .rawPassword(genPassword)
        .redirectUrl(resetPasswordUserDto.getRedirectUrl())
        .locale(localeContextHelper.toLocale(user.getLangiso())).build();
    //@formatter:on
    simpleChangePasswordMailSender.send(criteria);
    return null;
  }
}
