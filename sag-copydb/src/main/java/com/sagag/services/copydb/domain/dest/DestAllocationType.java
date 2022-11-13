package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestAllocationType.findAll", query = "SELECT a FROM DestAllocationType a")
@Data
public class DestAllocationType implements Serializable {

  private static final long serialVersionUID = -7730219503585154658L;

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
