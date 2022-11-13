package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestDeliveryType.findAll", query = "SELECT d FROM DestDeliveryType d")
@Data
public class DestDeliveryType implements Serializable {

  private static final long serialVersionUID = -8571362275760084796L;

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
