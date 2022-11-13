package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ORGANISATION")
@NamedQuery(name = "DestOrganisation.findAll", query = "SELECT o FROM DestOrganisation o")
@Data
public class DestOrganisation implements Serializable {

  private static final long serialVersionUID = 5374541200236169781L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ORDER_SETTINGS_ID")
  private Integer orderSettingsId;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @Column(name = "ORGTYPE_ID")
  private Integer orgtypeId;

  @Column(name = "PARENT_ID")
  private Integer parentId;

  @Column(name = "SHORTNAME")
  private String shortname;
}
