package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the COLLECTIVE_DELIVERY database table.
 * 
 */
@Entity
@Table(name = "COLLECTIVE_DELIVERY")
@NamedQuery(name = "DestCollectiveDelivery.findAll", query = "SELECT c FROM DestCollectiveDelivery c")
@Data
public class DestCollectiveDelivery implements Serializable {

  private static final long serialVersionUID = -4071327506228364253L;

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
