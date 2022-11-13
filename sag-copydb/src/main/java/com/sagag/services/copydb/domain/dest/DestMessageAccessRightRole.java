package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_ACCESS_RIGHT_ROLE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_ACCESS_RIGHT_ROLE")
@NamedQuery(name = "DestMessageAccessRightRole.findAll", query = "SELECT m FROM DestMessageAccessRightRole m")
@Data
public class DestMessageAccessRightRole implements Serializable {

  private static final long serialVersionUID = -3519072005460191378L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ESHOP_ROLE_ID")
  private Integer eshopRoleId;

  @Column(name = "MESSAGE_ACCESS_RIGHT_ID")
  private Integer messageAccessRightId;

}
