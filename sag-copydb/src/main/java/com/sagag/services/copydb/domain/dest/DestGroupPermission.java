package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the GROUP_PERMISSION database table.
 * 
 */
@Entity
@Table(name = "GROUP_PERMISSION")
@NamedQuery(name = "DestGroupPermission.findAll", query = "SELECT g FROM DestGroupPermission g")
@Data
public class DestGroupPermission implements Serializable {

  private static final long serialVersionUID = -8605430384347383076L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ALLOWED")
  private Boolean allowed;

  @Column(name = "GROUP_ID")
  private Long groupId;

  @Column(name = "PERM_ID")
  private Long permId;

}
