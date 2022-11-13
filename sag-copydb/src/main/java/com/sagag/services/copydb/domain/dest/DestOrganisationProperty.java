package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestOrganisationProperty.findAll", query = "SELECT o FROM DestOrganisationProperty o")
@Data
public class DestOrganisationProperty implements Serializable {

  private static final long serialVersionUID = -2759805942767618527L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "[VALUE]")
  private String value;

  @Column(name = "ORGANISATION_ID")
  private Long organisationId;

  @Column(name = "[TYPE]")
  private String type;

}
