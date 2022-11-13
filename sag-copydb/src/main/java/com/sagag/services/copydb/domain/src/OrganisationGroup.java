package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORGANISATION_GROUP database table.
 * 
 */
@Entity
@Table(name = "ORGANISATION_GROUP")
@NamedQuery(name = "OrganisationGroup.findAll", query = "SELECT o FROM OrganisationGroup o")
@Data
public class OrganisationGroup implements Serializable {

  private static final long serialVersionUID = -2887228030136984692L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

}
