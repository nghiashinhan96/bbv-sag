package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Serializable {

  private static final long serialVersionUID = 1767955840051517506L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Embedded
  private PasswordHash passwordHash;

  @Column(name = "IS_USER_ACTIVE")
  private boolean isUserActive;

  @OneToOne
  @JoinColumn(name = "USER_ID")
  @JsonBackReference
  private EshopUser eshopUser;

  @Column(name = "FIRST_LOGIN_DATE")
  private Date firstLoginDate;

  @Column(name = "LAST_ON_BEHALF_OF_DATE")
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
    return StringUtils.defaultString(passwordHash.getPassword());
  }
}
