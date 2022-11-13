package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestOrganisationType.findAll", query = "SELECT o FROM DestOrganisationType o")
@Data
public class DestOrganisationType implements Serializable {

  private static final long serialVersionUID = -5917882850495713224L;

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
