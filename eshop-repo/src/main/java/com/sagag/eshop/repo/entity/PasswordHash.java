package com.sagag.eshop.repo.entity;

import com.sagag.services.common.enums.HashType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordHash implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "PASSWORD")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "HASH_TYPE")
  private HashType hashType;

  @Column(name = "PASSWORD_HASH")
  private String passwordHash;

  @Column(name = "PASSWORD_SALT")
  private String passwordSalt;

  public static PasswordHash ofPasswordAsString(String password, HashType hashType) {
    return new PasswordHash(password, hashType, null, null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PasswordHash that = (PasswordHash) o;
    if (password != null ? !password.equals(that.password) : that.password != null) {
      return false;
    }
    return hashType == that.hashType;
  }

  @Override
  public int hashCode() {
    int result = password != null ? password.hashCode() : 0;
    result = 31 * result + (hashType != null ? hashType.hashCode() : 0);
    return result;
  }

}
