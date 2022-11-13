package com.sagag.services.tools.security.crypto.crypt3;

import com.sagag.services.tools.support.HashType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class Crypt3PasswordEncoder implements BasePasswordEncoder {

  @Override
  public String encode(CharSequence rawPassword) {
    return Crypt3.cryptPassword(rawPassword);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    final String hashedPassword = Crypt3.cryptPassword(rawPassword);
    if (StringUtils.equals(hashedPassword, encodedPassword)) {
      return true;
    }
    if (!hashedPassword.startsWith(encodedPassword)) {
      return false;
    }
    return StringUtils.equals(Crypt3.getFirstHashBlock(hashedPassword), encodedPassword);
  }

  @Override
  public HashType hashType() {
    return HashType.BLCK_VAR;
  }

}
