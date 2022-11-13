package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * Entity mapping class to table MESSAGE_LOCATION_RELATION.
 */
@Entity
@Table(name = "MESSAGE_LOCATION_RELATION")
@NamedQuery(name = "DestMessageLocationRelation.findAll", query = "SELECT m FROM DestMessageLocationRelation m")
@Data
public class DestMessageLocationRelation implements Serializable {

  private static final long serialVersionUID = 3986177497717341133L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "MESSAGE_ID")
  private Long messageId;

  @Column(name = "MESSAGE_LOCATION_ID")
  private Integer messageLocationId;

}
