package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_ROLE_TYPE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_ROLE_TYPE")
@NamedQuery(name = "DestMessageRoleType.findAll", query = "SELECT m FROM DestMessageRoleType m")
@Data
public class DestMessageRoleType implements Serializable {

  private static final long serialVersionUID = 959077936439729422L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ROLE_TYPE")
  private String roleType;

}
