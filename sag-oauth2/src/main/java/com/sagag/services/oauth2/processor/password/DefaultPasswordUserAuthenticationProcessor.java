package com.sagag.services.oauth2.processor.password;

import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.password.DefaultPasswordHashBuilder;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.processor.PasswordUserAuthenticationProcessor;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
public class DefaultPasswordUserAuthenticationProcessor
  implements PasswordUserAuthenticationProcessor {

  @Autowired
  private LoginService loginService;

  @Autowired
  private DefaultPasswordHashBuilder passwordHashBuilder;

  @Override
  @Transactional
  public EshopUserDetails process(EshopUserDetails details, Object... args) {
    if (ArrayUtils.isEmpty(args)) {
      return details;
    }
    final String rawPassword = (String) args[0];
    final HashType hashType = (HashType) args[1];
    // No update password for other hash types.
    if (hashType == null || HashType.SHA_512 != hashType) {
      return details;
    }

    log.info("Encoding password of APM user is using SHA-512 with current algorithm is BLCK_VAR");
    final long userId = details.getId();
    final Login login = loginService.getLoginForUser(userId);
    login.setPasswordHash(passwordHashBuilder.buildPasswordHash(rawPassword));
    loginService.update(login);
    return details;
  }

}
