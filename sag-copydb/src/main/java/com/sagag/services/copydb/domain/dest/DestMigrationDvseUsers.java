package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_DVSE_USERS database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_DVSE_USERS")
@NamedQuery(name = "DestMigrationDvseUsers.findAll", query = "SELECT m FROM DestMigrationDvseUsers m")
@Data
public class DestMigrationDvseUsers implements Serializable {

  private static final long serialVersionUID = 4854618425378582091L;

  @Column(name = "[ADMIN]")
  private short admin;

  @Column(name = "AFFILIATE")
  private String affiliate;

  @Column(name = "BLOCKED_BY_ERP")
  private short blockedByErp;

  @Column(name = "CATALOG_LOCKED")
  private short catalogLocked;

  @Column(name = "CONFIRMATION_SENT_AT")
  private String confirmationSentAt;

  @Column(name = "CONFIRMATION_TOKEN")
  private String confirmationToken;

  @Column(name = "CONFIRMED_AT")
  private String confirmedAt;

  @Column(name = "CREATED_AT")
  private String createdAt;

  @Column(name = "CURRENT_SIGN_IN_AT")
  private String currentSignInAt;

  @Column(name = "CURRENT_SIGN_IN_IP")
  private String currentSignInIp;

  @Column(name = "CUSTOMER_ID")
  private Integer customerId;

  @Column(name = "CUSTOMER_NUMBER")
  private String customerNumber;

  @Column(name = "DVSE_CUSTOMER_ID")
  private String dvseCustomerId;

  @Column(name = "DVSE_CUSTOMER_NUMBER")
  private String dvseCustomerNumber;

  @Column(name = "DVSE_ESID")
  private String dvseEsid;

  @Column(name = "DVSE_LAST_ACTIVE_AT")
  private String dvseLastActiveAt;

  @Column(name = "DVSE_PASSWORD")
  private String dvsePassword;

  @Column(name = "DVSE_USERNAME")
  private String dvseUsername;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "ENABLE_NET_PRICES")
  private short enableNetPrices;

  @Column(name = "ENABLE_PAYMENT_TYPE_CREDIT")
  private short enablePaymentTypeCredit;

  @Column(name = "ENCRYPTED_PASSWORD")
  private String encryptedPassword;

  @Column(name = "FAILED_ATTEMPTS")
  private Integer failedAttempts;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "IS_MIGRATED")
  private Boolean isMigrated;

  @Column(name = "[LANGUAGE]")
  private String language;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "LAST_SIGN_IN_AT")
  private String lastSignInAt;

  @Column(name = "LAST_SIGN_IN_IP")
  private String lastSignInIp;

  @Column(name = "LOCKED_AT")
  private String lockedAt;

  @Column(name = "MEGA_ULTRA_ROOT")
  private short megaUltraRoot;

  @Column(name = "OLD_ID")
  private Integer oldId;

  @Column(name = "OLD_ID1")
  private Integer oldId1;

  @Column(name = "PHONE")
  private String phone;

  @Column(name = "REMEMBER_CREATED_AT")
  private String rememberCreatedAt;

  @Column(name = "RESET_PASSWORD_SENT_AT")
  private String resetPasswordSentAt;

  @Column(name = "RESET_PASSWORD_TOKEN")
  private String resetPasswordToken;

  @Column(name = "SALUTATION")
  private String salutation;

  @Column(name = "SIGN_IN_COUNT")
  private Integer signInCount;

  @Column(name = "UNCONFIRMED_EMAIL")
  private String unconfirmedEmail;

  @Column(name = "UNLOCK_TOKEN")
  private String unlockToken;

  @Column(name = "UPDATED_AT")
  private String updatedAt;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "ZIP")
  private String zip;

}
