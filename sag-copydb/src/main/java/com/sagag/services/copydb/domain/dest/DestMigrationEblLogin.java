package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_EBL_LOGIN database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EBL_LOGIN")
@NamedQuery(name = "DestMigrationEblLogin.findAll", query = "SELECT m FROM DestMigrationEblLogin m")
@Data
public class DestMigrationEblLogin implements Serializable {

  private static final long serialVersionUID = -8095916260888438328L;

  @Column(name = "ACCESSPOINT_ID")
  private Long accesspointId;

  @Column(name = "DC")
  private Date dc;

  @Column(name = "DM")
  private Date dm;

  @Column(name = "HASHTYPE")
  private String hashtype;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "LASTLOGIN")
  private Date lastlogin;

  @Column(name = "ORGANISATION_ID")
  private Long organisationId;

  @Column(name = "PASSWORDCHANGEREQUIRED")
  private Long passwordchangerequired;

  @Column(name = "PASSWORDHASH")
  private String passwordhash;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "UC_ID")
  private Long ucId;

  @Column(name = "UM_ID")
  private Long umId;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "[VERSION]")
  private Long version;

}
