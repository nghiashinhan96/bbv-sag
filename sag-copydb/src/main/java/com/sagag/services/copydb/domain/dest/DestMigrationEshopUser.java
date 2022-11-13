package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_ESHOP_USER database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_ESHOP_USER")
@NamedQuery(name = "DestMigrationEshopUser.findAll", query = "SELECT m FROM DestMigrationEshopUser m")
@Data
public class DestMigrationEshopUser implements Serializable {

  private static final long serialVersionUID = -3454783618406941855L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "AGE")
  private Integer age;

  @Column(name = "DEFAULT_SETTING")
  private Boolean defaultSetting;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "EMAIL_CONFIRMATION")
  private Boolean emailConfirmation;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "HAS_PARTNER_PROGRAM")
  private Boolean hasPartnerProgram;

  @Column(name = "HAS_PARTNER_PROGRAM_LOGIN")
  private Boolean hasPartnerProgramLogin;

  @Column(name = "HOURLY_RATE")
  private BigDecimal hourlyRate;

  @Column(name = "IS_MIGRATED")
  private Boolean isMigrated;

  @Column(name = "[LANGUAGE]")
  private Integer language;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "NET_PRICE_CONFIRM")
  private Boolean netPriceConfirm;

  @Column(name = "NET_PRICE_VIEW")
  private Boolean netPriceView;

  @Column(name = "NEW_USER_ID")
  private Long newUserId;

  @Column(name = "NEWSLETTER")
  private Boolean newsletter;

  @Column(name = "ORGANISATION_ID")
  private String organisationId;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "SALUTATION")
  private Integer salutation;

  @Column(name = "SETTING")
  private Integer setting;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_ROLE")
  private String userRole;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "VAT_CONFIRM")
  private Boolean vatConfirm;

}
