package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "LOGIN")
@Entity
@NamedQuery(name = "Login.findAll", query = "SELECT l FROM Login l")
@Data
@EqualsAndHashCode(exclude = { "eshopUser" })
@ToString(exclude = { "eshopUser" })
public class Login implements Serializable {

  private static final long serialVersionUID = 1767955840051517506L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Embedded
  private PasswordHash passwordHash;

  @Column(name = "IS_USER_ACTIVE")
  private boolean isUserActive;

  @Column(name = "USER_ID", insertable = false, updatable = false)
  private Long userId;

  @OneToOne
  @JoinColumn(name = "USER_ID")
  @JsonBackReference
  private EshopUser eshopUser;

  private Date firstLoginDate;

  private Date lastOnBehalfOfDate;

  /**
   * Returns non null password in case the column is null.
   * <p>
   * to avoid the null attribute password to security constructor user
   *
   * @return the non null password
   */
  public String getNonNullPassword() {
    if (Objects.isNull(passwordHash)) {
      return StringUtils.EMPTY;
    }
    final String pwd = passwordHash.getPassword();
    if (StringUtils.isBlank(pwd)) {
      return StringUtils.EMPTY;
    }
    return pwd;
  }

}
