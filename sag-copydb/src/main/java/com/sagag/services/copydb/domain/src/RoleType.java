package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ROLE_TYPE database table.
 * 
 */
@Entity
@Table(name = "ROLE_TYPE")
@NamedQuery(name = "RoleType.findAll", query = "SELECT r FROM RoleType r")
@Data
public class RoleType implements Serializable {

  private static final long serialVersionUID = -679692643419822923L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "NAME")
  private String name;

}
