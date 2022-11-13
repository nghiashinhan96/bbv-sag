package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "MessageArea.findAll", query = "SELECT m FROM MessageArea m")
@Data
public class MessageArea implements Serializable {

  private static final long serialVersionUID = -7389677797324330L;

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
