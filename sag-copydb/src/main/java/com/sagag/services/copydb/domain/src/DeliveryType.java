package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the DELIVERY_TYPE database table.
 * 
 */
@Entity
@Table(name = "DELIVERY_TYPE")
@NamedQuery(name = "DeliveryType.findAll", query = "SELECT d FROM DeliveryType d")
@Data
public class DeliveryType implements Serializable {

  private static final long serialVersionUID = -2960974956809963245L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESC_CODE")
  private String descCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[TYPE]")
  private String type;

}
