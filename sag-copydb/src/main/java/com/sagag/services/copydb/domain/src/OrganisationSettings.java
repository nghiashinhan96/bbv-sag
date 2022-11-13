package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORGANISATION_SETTINGS database table.
 * 
 */
@Entity
@Table(name = "ORGANISATION_SETTINGS")
@NamedQuery(name = "OrganisationSettings.findAll", query = "SELECT o FROM OrganisationSettings o")
@Data
public class OrganisationSettings implements Serializable {

  private static final long serialVersionUID = -3353905727993225254L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "SETTING_KEY")
  private String settingKey;

  @Column(name = "SETTING_VALUE")
  private String settingValue;

}
