package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_CLIENT_RESOURCE database table.
 * 
 */
@Entity
@Table(name = "ESHOP_CLIENT_RESOURCE")
@NamedQuery(name = "EshopClientResource.findAll", query = "SELECT e FROM EshopClientResource e")
@Data
public class EshopClientResource implements Serializable {

  private static final long serialVersionUID = 1750632332654053129L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ACTIVE")
  private Boolean active;
}
