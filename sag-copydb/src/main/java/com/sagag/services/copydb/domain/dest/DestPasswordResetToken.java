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
 * The persistent class for the PASSWORD_RESET_TOKEN database table.
 * 
 */
@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
@NamedQuery(name = "DestPasswordResetToken.findAll", query = "SELECT p FROM DestPasswordResetToken p")
@Data
public class DestPasswordResetToken implements Serializable {

  private static final long serialVersionUID = -6104708855250953123L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "expiry_date")
  private Date expiryDate;

  @Column(name = "hash_username_code")
  private String hashUsernameCode;

  private String token;

  @Column(name = "USER_ID")
  private Long userId;

}
