package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the SALUTATION database table.
 * 
 */
@Entity
@Table(name = "SALUTATION")
@NamedQuery(name = "DestSalutation.findAll", query = "SELECT s FROM DestSalutation s")
@Data
public class DestSalutation implements Serializable {

  private static final long serialVersionUID = -952636110473215425L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[TYPE]")
  private String type;

}
