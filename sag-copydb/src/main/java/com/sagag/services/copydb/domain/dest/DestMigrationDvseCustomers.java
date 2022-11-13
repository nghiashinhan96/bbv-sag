package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_DVSE_CUSTOMERS database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_DVSE_CUSTOMERS")
@NamedQuery(name = "DestMigrationDvseCustomers.findAll", query = "SELECT m FROM DestMigrationDvseCustomers m")
@Data
public class DestMigrationDvseCustomers implements Serializable {

  private static final long serialVersionUID = 8163011067685569776L;

  @Column(name = "CUSTOMER_GROUP")
  private String customerGroup;

  @Column(name = "CUSTOMER_NUMBER")
  private Integer customerNumber;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "IS_MIGRATED")
  private Boolean isMigrated;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "PASSANT_NAME")
  private String passantName;

  @Column(name = "[RESULT]")
  private String result;

  @Column(name = "SALE_GROUP")
  private String saleGroup;

  @Column(name = "SEND_METHOD")
  private String sendMethod;

  @Column(name = "ZIP_CODE")
  private String zipCode;

}
