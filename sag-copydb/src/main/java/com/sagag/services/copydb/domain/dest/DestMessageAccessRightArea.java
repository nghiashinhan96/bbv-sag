package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_ACCESS_RIGHT_AREA database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_ACCESS_RIGHT_AREA")
@NamedQuery(name = "DestMessageAccessRightArea.findAll", query = "SELECT m FROM DestMessageAccessRightArea m")
@Data
public class DestMessageAccessRightArea implements Serializable {

  private static final long serialVersionUID = 3404551926871768853L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "MESSAGE_ACCESS_RIGHT_ID")
  private Integer messageAccessRightId;

  @Column(name = "MESSAGE_AREA_ID")
  private Integer messageAreaId;

}
