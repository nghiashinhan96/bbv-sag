package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORGANISATION_COLLECTION database table.
 * 
 */
@Entity
@Table(name = "ORGANISATION_COLLECTION")
@NamedQuery(name = "DestOrganisationCollection.findAll", query = "SELECT o FROM DestOrganisationCollection o")
@Data
public class DestOrganisationCollection implements Serializable {

  private static final long serialVersionUID = -7018144848443104165L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "SHORTNAME")
  private String shortname;

  @Column(name = "AFFILIATE_ID")
  private Integer affiliateId;

  @Column(name = "DESCRIPTION")
  private String description;
}
