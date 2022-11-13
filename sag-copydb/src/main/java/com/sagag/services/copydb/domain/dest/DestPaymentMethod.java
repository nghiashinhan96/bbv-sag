package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the PAYMENT_METHOD database table.
 * 
 */
@Entity
@Table(name = "PAYMENT_METHOD")
@NamedQuery(name = "DestPaymentMethod.findAll", query = "SELECT p FROM DestPaymentMethod p")
@Data
public class DestPaymentMethod implements Serializable {

  private static final long serialVersionUID = 8259085814126079303L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESC_CODE")
  private String descCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ORDER_DISPLAY")
  private Integer orderDisplay;

  @Column(name = "PAY_METHOD")
  private String payMethod;

}
