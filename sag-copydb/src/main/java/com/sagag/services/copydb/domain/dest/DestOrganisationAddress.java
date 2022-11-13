package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ORGANISATION_ADDRESS")
@NamedQuery(name = "DestOrganisationAddress.findAll", query = "SELECT o FROM DestOrganisationAddress o")
@Data
public class DestOrganisationAddress implements Serializable {

  private static final long serialVersionUID = 5132990583034090861L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "ADDRESS_ID")
  private Integer addressId;

}
