package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_CLIENT database table.
 * 
 */
@Entity
@Table(name = "ESHOP_CLIENT")
@NamedQuery(name = "EshopClient.findAll", query = "SELECT e FROM EshopClient e")
@Data
public class EshopClient implements Serializable {

  private static final long serialVersionUID = -422322643217505610L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CLIENT_NAME")
  private String clientName;

  @Column(name = "CLIENT_SECRET")
  private String clientSecret;

  @Column(name = "RESOURCE_ID")
  private Integer resourceId;

  @Column(name = "ACTIVE")
  private Boolean active;
}
