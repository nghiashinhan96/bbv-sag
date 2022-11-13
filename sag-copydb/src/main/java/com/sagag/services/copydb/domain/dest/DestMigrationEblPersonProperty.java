package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MIGRATION_EBL_PERSON_PROPERTY database table.
 * 
 */
@Entity
@Table(name = "MIGRATION_EBL_PERSON_PROPERTY")
@NamedQuery(name = "DestMigrationEblPersonProperty.findAll", query = "SELECT m FROM DestMigrationEblPersonProperty m")
@Data
public class DestMigrationEblPersonProperty implements Serializable {

  private static final long serialVersionUID = -7146861750471855548L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "[VALUE]")
  private String value;

}
