package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORGANISATION_TYPE database table.
 * 
 */
@Entity
@Table(name = "ORGANISATION_TYPE")
@NamedQuery(name = "OrganisationType.findAll", query = "SELECT o FROM OrganisationType o")
@Data
public class OrganisationType implements Serializable {

  private static final long serialVersionUID = 7795155797687156785L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[LEVEL]")
  private Integer level;

  @Column(name = "NAME")
  private String name;

}
