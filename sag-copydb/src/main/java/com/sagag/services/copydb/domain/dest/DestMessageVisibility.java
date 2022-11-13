package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_VISIBILITY database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_VISIBILITY")
@NamedQuery(name = "DestMessageVisibility.findAll", query = "SELECT m FROM DestMessageVisibility m")
@Data
public class DestMessageVisibility implements Serializable {

  private static final long serialVersionUID = -6220720021863357975L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "VISIBILITY")
  private String visibility;

}
