package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_LOCATION_TYPE_ROLE_TYPE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_LOCATION_TYPE_ROLE_TYPE")
@NamedQuery(name = "DestMessageLocationTypeRoleType.findAll", query = "SELECT m FROM DestMessageLocationTypeRoleType m")
@Data
public class DestMessageLocationTypeRoleType implements Serializable {

  private static final long serialVersionUID = -3289813940164987878L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "MESSAGE_LOCATION_TYPE_ID")
  private Integer messageLocationTypeId;

  @Column(name = "MESSAGE_ROLE_TYPE_ID")
  private Integer messageRoleTypeId;

}
