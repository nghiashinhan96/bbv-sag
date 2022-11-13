package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_EXTERNAL_USER database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EXTERNAL_USER")
@NamedQuery(name = "DestMigrationExternalUser.findAll", query = "SELECT m FROM DestMigrationExternalUser m")
@Data
public class DestMigrationExternalUser implements Serializable {

  private static final long serialVersionUID = -6680312383920227397L;

  @Column(name = "AFFILIATE")
  private String affiliate;

  @Column(name = "DVSE_PASSWORD")
  private String dvsePassword;

  @Column(name = "DVSE_USERNAME")
  private String dvseUsername;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "PARENT_ID")
  private Integer parentId;

  @Column(name = "USERNAME")
  private String username;

}
