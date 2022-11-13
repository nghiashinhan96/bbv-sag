package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_LOCATION_TYPE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_LOCATION_TYPE")
@NamedQuery(name = "DestMessageLocationType.findAll", query = "SELECT m FROM DestMessageLocationType m")
@Data
public class DestMessageLocationType implements Serializable {

  private static final long serialVersionUID = -7391357899235476845L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "LOCATION_TYPE")
  private String locationType;

}
