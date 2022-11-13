package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORDER_TYPE database table.
 * 
 */
@Entity
@Table(name = "ORDER_TYPE")
@NamedQuery(name = "DestOrderType.findAll", query = "SELECT o FROM DestOrderType o")
@Data
public class DestOrderType implements Serializable {

  private static final long serialVersionUID = 5224604982682611490L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESC_CODE")
  private String descCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ORDER_TYPE")
  private String orderType;

}
