package com.sagag.services.tools.domain.target;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sagag.services.tools.support.HashType;

@Embeddable
@Data
@NoArgsConstructor
public class PasswordHash implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "PASSWORD")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "HASH_TYPE")
  private HashType hashType;

  public PasswordHash(String password) {
    this(password, HashType.BLCK_VAR);
  }

  public PasswordHash(String password, HashType hashType) {
    this.password = password;
    this.hashType = hashType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    PasswordHash that = (PasswordHash) o;

    if (password != null ? !password.equals(that.password) : that.password != null)
      return false;
    if (hashType != that.hashType)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = password != null ? password.hashCode() : 0;
    result = 31 * result + (hashType != null ? hashType.hashCode() : 0);
    return result;
  }

}
