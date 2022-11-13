package com.sagag.services.service.user.password.reset;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.repo.password.DefaultPasswordHashBuilder;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import com.sagag.services.service.mail.ChangePasswordCriteria;
import com.sagag.services.service.mail.ChangePasswordMailSender;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class SelfEshopUserResetPasswordHandler extends AbstractResetPasswordHandler
  implements ResetPasswordHandler<EshopUserLoginDto, Void> {

  @Autowired
  private UserService userService;

  @Autowired
  private LoginService loginService;

  @Autowired
  private ChangePasswordMailSender changePasswordMailSender;

  @Autowired
  private DefaultPasswordHashBuilder passwordHashBuilder;

  @Override
  @Transactional
  public Void handle(EshopUserLoginDto eshopUserLoginDto) throws ServiceException {
    final PasswordResetToken passwordResetToken =
      getPasswordResetTokenByTokenAndHash(eshopUserLoginDto.getToken(),
        eshopUserLoginDto.getHashUsernameCode());
    final EshopUser user = passwordResetToken.getUser();
    resetPassword(user, eshopUserLoginDto);
    // delete after reset password successfully
    deletePasswordResetToken(passwordResetToken);
    return null;
  }

  private void resetPassword(final EshopUser user, final EshopUserLoginDto dto) {
    // fromEmail
    final String affiliateEmail = findDefaultAffiliateEmail(dto.getAffiliate(), dto.isFinalUser());

    final long userId = user.getId();
    final Login login = loginService.getLoginForUser(userId);
    login.setPasswordHash(passwordHashBuilder.buildPasswordHash(dto.getPassword()));

    // update password
    loginService.update(login);

    final EshopUser eshopUser = userService.getUserById(userId);
    ChangePasswordCriteria criteria = ChangePasswordCriteria.buildResetPaswordEmail(eshopUser,
      affiliateEmail, StringUtils.EMPTY, true,
      localeContextHelper.toLocale(eshopUser.getNonNullLangIso()));
    criteria.setFinalUser(dto.isFinalUser());
    changePasswordMailSender.send(criteria);
  }

  private PasswordResetToken getPasswordResetTokenByTokenAndHash(final String token,
    final String hash) {
    return passwordTokenRepo.findByTokenAndHashUsernameCode(token, hash);
  }

  private void deletePasswordResetToken(final PasswordResetToken passwordResetToken) {
    passwordTokenRepo.delete(passwordResetToken);
    log.debug("Token deleted is {}", passwordResetToken.getToken());
  }
}
