package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_ROLE database table.
 * 
 */
@Entity
@Table(name = "ESHOP_ROLE")
@NamedQuery(name = "DestEshopRole.findAll", query = "SELECT e FROM DestEshopRole e")
@Data
public class DestEshopRole implements Serializable {

  private static final long serialVersionUID = 4695323422893429401L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ROLE_TYPE_ID")
  private Integer roleTypeId;

  @Column(name = "DESCRIPTION")
  private String description;

}
