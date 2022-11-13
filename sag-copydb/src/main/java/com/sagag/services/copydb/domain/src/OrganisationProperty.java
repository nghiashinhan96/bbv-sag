package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORGANISATION_PROPERTY database table.
 * 
 */
@Entity
@Table(name = "ORGANISATION_PROPERTY")
@NamedQuery(name = "OrganisationProperty.findAll", query = "SELECT o FROM OrganisationProperty o")
@Data
public class OrganisationProperty implements Serializable {

  private static final long serialVersionUID = -5998193222374858331L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ORGANISATION_ID")
  private Long organisationId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "VALUE")
  private String value;

}
