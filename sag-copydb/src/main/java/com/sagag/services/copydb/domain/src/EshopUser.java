package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ESHOP_USER")
@NamedQuery(name = "EshopUser.findAll", query = "SELECT e FROM EshopUser e")
@Data
public class EshopUser implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "EMAIL_CONFIRMATION")
  private Boolean emailConfirmation;

  @Column(name = "FAX")
  private String fax;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "HOURLY_RATE")
  private BigDecimal hourlyRate;

  @Column(name = "LANGUAGE")
  private Integer language;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "NEWSLETTER")
  private Boolean newsletter;

  @Column(name = "ORIGINAL_USER_ID")
  private Long originalUserId;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "SALUTATION")
  private Integer salutation;

  @Column(name = "SETTING")
  private Integer setting;

  @Column(name = "SIGN_IN_DATE")
  private Date signInDate;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "VAT_CONFIRM")
  private Boolean vatConfirm;

}
