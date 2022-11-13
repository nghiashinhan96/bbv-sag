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
 * The persistent class for the MIGRATION_EBL_PERSON database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EBL_PERSON")
@NamedQuery(name = "DestMigrationEblPerson.findAll", query = "SELECT m FROM DestMigrationEblPerson m")
@Data
public class DestMigrationEblPerson implements Serializable {

  private static final long serialVersionUID = 1436571431135350387L;

  @Column(name = "AFFILIATE")
  private String affiliate;

  @Column(name = "DC")
  private Date dc;

  @Column(name = "DM")
  private Date dm;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "FIRSTNAME")
  private String firstname;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "IS_MIGRATED")
  private Boolean isMigrated;

  @Column(name = "LANGISO")
  private String langiso;

  @Column(name = "LASTNAME")
  private String lastname;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "UC_ID")
  private Long ucId;

  @Column(name = "UM_ID")
  private Long umId;

  @Column(name = "[VERSION]")
  private Integer version;

}
