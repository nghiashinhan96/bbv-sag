package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_EBL_LOGIN_ROLEASSIGNMENT database
 * table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EBL_LOGIN_ROLEASSIGNMENT")
@NamedQuery(name = "DestMigrationEblLoginRoleassignment.findAll", query = "SELECT m FROM DestMigrationEblLoginRoleassignment m")
@Data
public class DestMigrationEblLoginRoleassignment implements Serializable {

  private static final long serialVersionUID = 447896260851629014L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "LOGIN_ID")
  private Long loginId;

  @Column(name = "ORGANISATION_ID")
  private Long organisationId;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "[ROLE]")
  private String role;

}
