package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_ACCESS_RIGHT database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_ACCESS_RIGHT")
@NamedQuery(name = "DestMessageAccessRight.findAll", query = "SELECT m FROM DestMessageAccessRight m")
@Data
public class DestMessageAccessRight implements Serializable {

  private static final long serialVersionUID = -7440375554142660092L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "MESSAGE_ROLE_TYPE_ID")
  private Integer messageRoleTypeId;

  @Column(name = "USER_GROUP")
  private String userGroup;

  @Column(name = "USER_GROUP_KEY")
  private String userGroupKey;

}
