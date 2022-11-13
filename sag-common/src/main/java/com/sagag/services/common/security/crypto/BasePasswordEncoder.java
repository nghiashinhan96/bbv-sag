package com.sagag.services.common.security.crypto;

import com.sagag.services.common.enums.HashType;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface BasePasswordEncoder extends PasswordEncoder {

  /**
   *
   */
  HashType hashType();

}
