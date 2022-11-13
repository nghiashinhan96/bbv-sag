package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestOrganisationGroup.findAll", query = "SELECT o FROM DestOrganisationGroup o")
@Data
public class DestOrganisationGroup implements Serializable {

  private static final long serialVersionUID = 803437216371420261L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

}
