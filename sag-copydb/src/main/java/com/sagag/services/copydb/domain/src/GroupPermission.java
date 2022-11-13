package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "GroupPermission.findAll", query = "SELECT g FROM GroupPermission g")
@Data
public class GroupPermission implements Serializable {

  private static final long serialVersionUID = -3857611412706232040L;

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
