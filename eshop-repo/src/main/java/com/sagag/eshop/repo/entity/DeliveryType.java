package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "DELIVERY_TYPE")
@Entity
@NamedQuery(name = "DeliveryType.findAll", query = "SELECT a FROM DeliveryType a")
@Data
public class DeliveryType implements Serializable {

  private static final long serialVersionUID = -3506928523760404340L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String descCode;

  private String type;

  private String description;

}
