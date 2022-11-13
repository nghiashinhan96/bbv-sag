package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "EshopGroup.findAll", query = "SELECT e FROM EshopGroup e")
@Data
public class EshopGroup implements Serializable {

  private static final long serialVersionUID = 8174878544275025390L;

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
