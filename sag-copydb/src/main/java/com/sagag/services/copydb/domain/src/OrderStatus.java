package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORDER_STATUS database table.
 * 
 */
@Entity
@Table(name = "ORDER_STATUS")
@NamedQuery(name = "OrderStatus.findAll", query = "SELECT o FROM OrderStatus o")
@Data
public class OrderStatus implements Serializable {

  private static final long serialVersionUID = 35377620563726729L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESC_CODE")
  private String descCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ORDER_STATUS")
  private String orderStatus;

}
