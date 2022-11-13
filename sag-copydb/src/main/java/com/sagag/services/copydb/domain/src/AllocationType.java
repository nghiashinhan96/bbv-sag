package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ALLOCATION_TYPE database table.
 * 
 */
@Entity
@Table(name = "ALLOCATION_TYPE")
@NamedQuery(name = "AllocationType.findAll", query = "SELECT a FROM AllocationType a")
@Data
public class AllocationType implements Serializable {

  private static final long serialVersionUID = -3697381522675067530L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESC_CODE")
  private String descCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[TYPE]")
  private String type;

}
