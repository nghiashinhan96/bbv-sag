package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_ORGANISATION database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_ORGANISATION")
@NamedQuery(name = "DestMigrationOrganisation.findAll", query = "SELECT m FROM DestMigrationOrganisation m")
@Data
public class DestMigrationOrganisation implements Serializable {

  private static final long serialVersionUID = 579068099803035438L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "COMPANY_NAME")
  private String companyName;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  @Column(name = "IS_MIGRATED")
  private Boolean isMigrated;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @Column(name = "ORG_ID")
  private Integer orgId;

}
