package com.sagag.services.common.security;

import com.sagag.services.common.enums.HashType;

public interface PasswordHashBuilder<T> {

  /**
   * Builds password hash.
   *
   * @param rawPassword
   * @param hashType
   * @return the password hash object
   */
  T buildPasswordHash(String rawPassword, HashType hashType);

  /**
   * Builds password hash.
   *
   * @param rawPassword
   * @return the password hash object
   */
  T buildPasswordHash(String rawPassword);

  /**
   * Builds password hash.
   *
   * @param passwordHash
   * @param passwordSalt
   * @param hashType
   * @return the password hash object
   */
  T buildPasswordHash(String passwordHash, String passwordSalt, HashType hashType);
}
