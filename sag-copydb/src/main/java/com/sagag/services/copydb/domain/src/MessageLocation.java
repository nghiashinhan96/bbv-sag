package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_LOCATION database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_LOCATION")
@NamedQuery(name = "MessageLocation.findAll", query = "SELECT m FROM MessageLocation m")
@Data
public class MessageLocation implements Serializable {

  private static final long serialVersionUID = 8023604081826479137L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "MESSAGE_LOCATION_TYPE_ID")
  private Integer messageLocationTypeId;

  @Column(name = "[VALUE]")
  private String value;

}
