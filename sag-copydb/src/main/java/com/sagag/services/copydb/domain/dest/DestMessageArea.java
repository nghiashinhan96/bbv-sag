package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_AREA database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_AREA")
@NamedQuery(name = "DestMessageArea.findAll", query = "SELECT m FROM DestMessageArea m")
@Data
public class DestMessageArea implements Serializable {

  private static final long serialVersionUID = -4266451906491357671L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "AREA")
  private String area;

  @Column(name = "AUTH")
  private Boolean auth;

  @Column(name = "DESCRIPTION")
  private String description;

}
