package com.sagag.services.service.user.password.reset;

import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import com.sagag.services.service.user.password.ResetPasswordTokenStatus;

import org.springframework.stereotype.Component;

@Component
public class SelfEshopUserValidateSecurityCodeResetPasswordHandler
  extends AbstractResetPasswordHandler
  implements ResetPasswordHandler<EshopUserLoginDto, ResetPasswordTokenStatus> {

  @Override
  public ResetPasswordTokenStatus handle(EshopUserLoginDto user) {
    final PasswordResetToken passwordResetToken = passwordTokenRepo
      .findByTokenAndHashUsernameCode(user.getToken(), user.getHashUsernameCode());
    if (passwordResetToken == null) {
      return ResetPasswordTokenStatus.TOKEN_INVALID;
    }
    if (passwordResetToken.isExpired()) {
      passwordTokenRepo.delete(passwordResetToken);
      return ResetPasswordTokenStatus.TOKEN_EXPIRED;
    }
    return ResetPasswordTokenStatus.TOKEN_VALID;
  }
}
