package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ESHOP_ROLE")
@NamedQuery(name = "EshopRole.findAll", query = "SELECT e FROM EshopRole e")
@Data
public class EshopRole implements Serializable {

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
