package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_GROUP database table.
 * 
 */
@Entity
@Table(name = "ESHOP_GROUP")
@NamedQuery(name = "DestEshopGroup.findAll", query = "SELECT e FROM DestEshopGroup e")
@Data
public class DestEshopGroup implements Serializable {

  private static final long serialVersionUID = 118596127530112733L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @Column(name = "ROLE_ID")
  private Integer roleId;

}
