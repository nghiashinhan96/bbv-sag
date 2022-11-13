package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sagag.services.common.security.crypto.bcrypt.BCrypt;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

/**
 * UT to verify for {@link BCrypt}.
 */
@Slf4j
public class PasswordBcryptTest {

  private static final String PEPPER =
      "a6a80d817f07465321a9f8b3e826b7734516147608525958c79b571a7f0c74e69202fd70806463f1aa01da75d17854797014ceec5f6f0f6fc4531242cc5b6a56";

  @Test
  public void testGetPassword123Hash() {
    String rawPassword = "123";
    String hashedPassword = null;
    hashedPassword = BCrypt.hashpw(rawPassword + PEPPER, BCrypt.gensalt());
    log.debug("testGetPassword123Hash() \t Raw password: {} \t\t Hashed password: {}", rawPassword,
        hashedPassword);

    assertNotNull(hashedPassword);
  }

  @Test
  public void testGetPasswordHash() {
    String rawPassword = "asdasd123123";
    String hashedPassword = null;
    hashedPassword = BCrypt.hashpw(rawPassword + PEPPER, BCrypt.gensalt());
    log.debug("testGetPasswordHash() \t\t Raw password: {} \t Hashed password: {}", rawPassword,
        hashedPassword);

    assertNotNull(hashedPassword);
  }

  @Test
  public void testCheckPasswordHash123() {
    String rawPassword = "123";
    String hashedPassword = "$2a$10$iYLIFBJNyodJT/xfB9.4guP7516LvQRluYpA68G.PSHxLKTqljKCK";
    log.debug("testCheckPasswordHash123() \t Raw password: {} \t\t Hashed password: {}",
        rawPassword, hashedPassword);

    assertTrue(BCrypt.checkpw(rawPassword + PEPPER, hashedPassword));
  }

  @Test
  public void testCheckPasswordHash() {
    String rawPassword = "asdasd123123";
    String hashedPassword = "$2a$10$wildn8o7tPj2UzxnT2ZAjuRHkgDxceNPlIN2Bh9JYEXOPXTki6EhW";
    log.debug("testCheckPasswordHash() \t\t Raw password: {} \t Hashed password: {}", rawPassword,
        hashedPassword);

    assertTrue(BCrypt.checkpw(rawPassword + PEPPER, hashedPassword));
  }

  @Test
  public void testCheckPasswordHashRuntime() {
    String rawPassword = "asdasd123123";
    String hashedPassword = BCrypt.hashpw(rawPassword + PEPPER, BCrypt.gensalt());
    log.debug("testCheckPasswordHashRuntime() \t Raw password: {} \t Hashed password: {}",
        rawPassword, hashedPassword);

    assertTrue(BCrypt.checkpw(rawPassword + PEPPER, hashedPassword));
  }

}
