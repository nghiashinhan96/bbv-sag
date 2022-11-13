package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "COLLECTIVE_DELIVERY")
@Entity
@NamedQuery(name = "CollectiveDelivery.findAll", query = "SELECT a FROM CollectiveDelivery a")
@Data
public class CollectiveDelivery implements Serializable {

  private static final long serialVersionUID = -677074165520726612L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String descCode;

  private String type;

  private String description;

}
