package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestLogin.findAll", query = "SELECT l FROM DestLogin l")
@Data
public class DestLogin implements Serializable {

  private static final long serialVersionUID = -3073318925564976715L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "FIRST_LOGIN_DATE")
  private Date firstLoginDate;

  @Column(name = "hash_type")
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
