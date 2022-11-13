package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "PaymentMethod.findAll", query = "SELECT p FROM PaymentMethod p")
@Data
public class PaymentMethod implements Serializable {

  private static final long serialVersionUID = 7296855014771091237L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESC_CODE")
  private String descCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PAY_METHOD")
  private String payMethod;

}
