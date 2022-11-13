package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the LOGIN database table.
 * 
 */
@Entity
@Table(name = "LOGIN")
@NamedQuery(name = "Login.findAll", query = "SELECT l FROM Login l")
@Data
public class Login implements Serializable {

  private static final long serialVersionUID = -9087821397459709916L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "FIRST_LOGIN_DATE")
  private Date firstLoginDate;

  @Column(name = "HASH_TYPE")
  private String hashType;

  @Column(name = "IS_USER_ACTIVE")
  private Boolean isUserActive;

  @Column(name = "LAST_ON_BEHALF_OF_DATE")
  private Date lastOnBehalfOfDate;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "PASSWORD_HASH_BACKUP")
  private String passwordHashBackup;

  @Column(name = "USER_ID")
  private Long userId;

}
