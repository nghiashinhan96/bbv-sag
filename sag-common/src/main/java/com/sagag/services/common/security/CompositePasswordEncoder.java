package com.sagag.services.common.security;

import com.sagag.services.common.affiliate.AffiliateConfigurationFactory;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.security.crypto.BasePasswordEncoder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is the composite for multiple password encoders.
 *
 * <pre>
 * We have some special cases need encode for client id, reset password code.
 * We also based on BLCK_VAR as default for all that cases.
 * </pre>
 */
@Component
@Slf4j
@Primary
public class CompositePasswordEncoder implements PasswordEncoder {

  @Autowired
  private AffiliateConfigurationFactory affiliateConfigFactory;

  @Autowired
  private List<BasePasswordEncoder> passwordEncoders;

  @Override
  @LogExecutionTime
  public String encode(CharSequence rawPassword) {
    return getPasswordEncoder(affiliateConfigFactory.getHashType()).encode(rawPassword);
  }

  @Override
  @LogExecutionTime
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (StringUtils.isBlank(encodedPassword)) {
      log.debug("Empty encoded password");
      throw new BadCredentialsException("The stored password is empty");
    }
    if (passwordEncoders.stream()
        .noneMatch(pwdEncoder -> pwdEncoder.matches(rawPassword, encodedPassword))) {
      throw new BadCredentialsException("Invalid password.");
    }
    return true;
  }

  public String encodeBcrypt(CharSequence rawPassword) {
    log.debug("Password hash: Bcrypt");
    return getPasswordEncoder(HashType.BCRYPT).encode(rawPassword);
  }

  public String encodeBlockVar(CharSequence rawPassword) {
    log.debug("Password hash: Crypt3");
    return getPasswordEncoder(HashType.BLCK_VAR).encode(rawPassword);
  }

  private PasswordEncoder getPasswordEncoder(HashType hashType) {
    return passwordEncoders.stream().filter(encoder -> encoder.hashType() == hashType)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not support this password encoder"));
  }

}
