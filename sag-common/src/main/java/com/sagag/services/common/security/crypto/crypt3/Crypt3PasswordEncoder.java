package com.sagag.services.common.security.crypto.crypt3;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.security.crypto.BasePasswordEncoder;

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

  public static void main(String... args) {
    String p1 = new Crypt3PasswordEncoder().encode("123456@A"); // Correct password
    String p2 = new Crypt3PasswordEncoder().encode("123456@Ay"); // Incorrect password
    boolean result = new Crypt3PasswordEncoder().matches("123456@Ay", "Fd2yqbE5FwWs2");

    String logs = String.format("p1 = %s - p2 = %s - result = %s", p1, p2, result);
    System.out.println(logs);

  }

}
