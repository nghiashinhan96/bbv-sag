package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the GROUP_ROLE database table.
 * 
 */
@Entity
@Table(name = "GROUP_ROLE")
@NamedQuery(name = "DestGroupRole.findAll", query = "SELECT g FROM DestGroupRole g")
@Data
public class DestGroupRole implements Serializable {

  private static final long serialVersionUID = -5121058735241806922L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Column(name = "ROLE_ID")
  private Integer roleId;

}
