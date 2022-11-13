package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the PAYMENT_TYPE database table.
 * 
 */
@Entity
@Table(name = "PAYMENT_TYPE")
@NamedQuery(name = "DestPaymentType.findAll", query = "SELECT p FROM DestPaymentType p")
@Data
public class DestPaymentType implements Serializable {

  private static final long serialVersionUID = 5611586143591173078L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "DESCRIPTION")
  private String description;

}
