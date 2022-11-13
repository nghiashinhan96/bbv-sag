package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_EBL_ORGANISATION database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EBL_ORGANISATION")
@NamedQuery(name = "DestMigrationEblOrganisation.findAll", query = "SELECT m FROM DestMigrationEblOrganisation m")
@Data
public class DestMigrationEblOrganisation implements Serializable {

  private static final long serialVersionUID = 521872702956911589L;

  @Column(name = "COMPANY_NAME")
  private String companyName;

  @Column(name = "COUNTRYISO")
  private String countryiso;

  @Column(name = "DC")
  private String dc;

  @Column(name = "DISTRIBUTIONSET")
  private String distributionset;

  @Column(name = "DM")
  private String dm;

  @Column(name = "ERPINSTANCE")
  private String erpinstance;

  @Column(name = "ERPNUMBER")
  private Long erpnumber;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "IS_MIGRATED")
  private Boolean isMigrated;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NEW_ID")
  private Long newId;

  @Column(name = "ORGANISATIONID")
  private String organisationid;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "UC_ID")
  private String ucId;

  @Column(name = "UM_ID")
  private String umId;

  @Column(name = "[VERSION]")
  private String version;

}
