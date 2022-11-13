package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_SUB_AREA database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_SUB_AREA")
@NamedQuery(name = "DestMessageSubArea.findAll", query = "SELECT m FROM DestMessageSubArea m")
@Data
public class DestMessageSubArea implements Serializable {

  private static final long serialVersionUID = 8196337652247533069L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "MESSAGE_AREA_ID")
  private Integer messageAreaId;

  @Column(name = "[SORT]")
  private short sort;

  @Column(name = "SUB_AREA")
  private String subArea;

}
