/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sagag.services.common.security.crypto.bcrypt;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.security.crypto.BasePasswordEncoder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Implementation of PasswordEncoder that uses the BCrypt strong hashing function. Clients can
 * optionally supply a "strength" (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The
 * larger the strength parameter the more work will have to be done (exponentially) to hash the
 * passwords. The default value is 10.
 *
 * @author Dave Syer
 *
 */
@Slf4j
@Component
public class BCryptPasswordEncoder implements BasePasswordEncoder {

  private static final Pattern BCRYPT_PATTERN = Pattern
      .compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

  private static final String PEPPER =
      "a6a80d817f07465321a9f8b3e826b7734516147608525958c79b571a7f0c74e69202fd70806463f1aa01da75d17854797014ceec5f6f0f6fc4531242cc5b6a56";

  @Override
  public String encode(CharSequence rawPassword) {
    return BCrypt.hashpw(rawPassword.toString() + PEPPER, BCrypt.gensalt());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
      log.debug("Encoded password does not look like BCrypt");
      return false;
    }
    return BCrypt.checkpw(rawPassword.toString() + PEPPER, encodedPassword);
  }

  @Override
  public HashType hashType() {
    return HashType.BCRYPT;
  }

}
