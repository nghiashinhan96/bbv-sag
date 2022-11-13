package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ROLE_PERMISSION database table.
 * 
 */
@Entity
@Table(name = "ROLE_PERMISSION")
@NamedQuery(name = "RolePermission.findAll", query = "SELECT r FROM RolePermission r")
@Data
public class RolePermission implements Serializable {

  private static final long serialVersionUID = 6615906247085316056L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ROLE_ID")
  private Integer roleId;

  @Column(name = "PERM_ID")
  private Integer permId;

}
