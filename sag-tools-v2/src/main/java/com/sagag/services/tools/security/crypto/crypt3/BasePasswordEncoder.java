package com.sagag.services.tools.security.crypto.crypt3;

import com.sagag.services.tools.support.HashType;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface BasePasswordEncoder extends PasswordEncoder {

  HashType hashType();

}
