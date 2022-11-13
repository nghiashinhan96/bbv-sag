package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_EXTERNAL_ORGANISATION database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EXTERNAL_ORGANISATION")
@NamedQuery(name = "DestMigrationExternalOrganisation.findAll", query = "SELECT m FROM DestMigrationExternalOrganisation m")
@Data
public class DestMigrationExternalOrganisation implements Serializable {

  private static final long serialVersionUID = -2897618515231713833L;

  @Column(name = "AFFILIATE")
  private String affiliate;

  @Column(name = "EXTERNAL_CUSTOMER_ID")
  private String externalCustomerId;

  @Column(name = "EXTERNAL_CUSTOMER_NAME")
  private String externalCustomerName;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @Column(name = "ORG_ID")
  private Integer orgId;

}
