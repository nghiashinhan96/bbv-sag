package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_STYLE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_STYLE")
@NamedQuery(name = "DestMessageStyle.findAll", query = "SELECT m FROM DestMessageStyle m")
@Data
public class DestMessageStyle implements Serializable {

  private static final long serialVersionUID = -8745033329392724975L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STYLE")
  private String style;

}
