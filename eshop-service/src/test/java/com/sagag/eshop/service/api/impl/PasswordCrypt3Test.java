package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sagag.services.common.security.crypto.crypt3.Crypt3;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * UT to verify for {@link Crypt3}.
 */
@Slf4j
public class PasswordCrypt3Test {

  @Test
  public void testGetPassword123Hash() {
    String rawPassword = "123";
    String hashedPassword = null;
    hashedPassword = Crypt3.cryptPassword(rawPassword);
    log.debug("testGetPassword123Hash() \t Raw password: {} \t\t Hashed password: {}", rawPassword,
        hashedPassword);

    assertNotNull(hashedPassword);
  }

  @Test
  public void testGetPasswordHash() {
    String rawPassword = "asdasd123123";
    String hashedPassword = null;
    hashedPassword = Crypt3.cryptPassword(rawPassword);
    log.debug("testGetPasswordHash() \t\t Raw password: {} \t Hashed password: {}", rawPassword,
        hashedPassword);

    assertNotNull(hashedPassword);
  }

  @Test
  public void testCheckPasswordHash123() {
    String rawPassword = "123";
    String hashedPassword = "Fdl8lcev2gXKE";
    log.debug("testCheckPasswordHash123() \t Raw password: {} \t\t Hashed password: {}",
        rawPassword, hashedPassword);

    assertTrue(StringUtils.equalsIgnoreCase(hashedPassword,
        Crypt3.cryptPassword(rawPassword)));
  }

  @Test
  public void testCheckPasswordHash() {
    String rawPassword = "asdasd123123";
    String hashedPassword = "Fdn5zO6igSyucFd2zrigX8ugE.";
    log.debug("testCheckPasswordHash() \t\t Raw password: {} \t Hashed password: {}", rawPassword,
        hashedPassword);

    assertTrue(StringUtils.equalsIgnoreCase(hashedPassword,
        Crypt3.cryptPassword(rawPassword)));
  }

}
