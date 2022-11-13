package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_EBL_ORGANISATION_PROPERTY database
 * table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EBL_ORGANISATION_PROPERTY")
@NamedQuery(name = "DestMigrationEblOrganisationProperty.findAll", query = "SELECT m FROM DestMigrationEblOrganisationProperty m")
@Data
public class DestMigrationEblOrganisationProperty implements Serializable {

  private static final long serialVersionUID = -8144428369132262489L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ORGANISATION_ID")
  private Long organisationId;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "[VALUE]")
  private String value;

}
