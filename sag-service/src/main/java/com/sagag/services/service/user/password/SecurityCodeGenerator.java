package com.sagag.services.service.user.password;

import com.sagag.eshop.repo.api.forgotpassword.PasswordResetTokenRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.security.CompositePasswordEncoder;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class SecurityCodeGenerator {

  @Autowired
  private PasswordResetTokenRepository passwordTokenRepository;

  @Autowired
  protected CompositePasswordEncoder passwordEncoder;

  public PasswordResetToken generateCode(EshopUser user) {
    // Generate new random code
    final String genCode = StringUtils.capitalize(
            RandomStringUtils.randomNumeric(SagConstants.DEFAULT_MIN_LENGTH_CODE));
    final String hashUsernameCode = generateHashUsernameCode(genCode, user);
    return createPasswordResetTokenForUser(user, genCode, hashUsernameCode);
  }

  private String generateHashUsernameCode(String code, EshopUser user) {
    final String usernameCode = String.join("-", user.getUsername(), code,
        String.valueOf(Calendar.getInstance().getTime().getTime()));
    return passwordEncoder.encodeBcrypt(usernameCode);
  }

  private PasswordResetToken createPasswordResetTokenForUser(final EshopUser user,
      final String token, final String hashUsernameCode) {
    final PasswordResetToken myToken = new PasswordResetToken(token, user, hashUsernameCode);
    return passwordTokenRepository.save(myToken);
  }
}
