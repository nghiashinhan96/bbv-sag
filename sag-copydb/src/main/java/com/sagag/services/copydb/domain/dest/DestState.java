package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the [STATE] database table.
 * 
 */
@Entity
@Table(name = "[STATE]")
@NamedQuery(name = "DestState.findAll", query = "SELECT s FROM DestState s")
@Data
public class DestState implements Serializable {

  private static final long serialVersionUID = 6716810367281577569L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "DESCRIPTION")
  private String description;

}
