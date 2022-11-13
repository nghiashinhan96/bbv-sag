package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the CURRENCY database table.
 * 
 */
@Entity
@Table(name = "CURRENCY")
@NamedQuery(name = "DestCurrency.findAll", query = "SELECT c FROM DestCurrency c")
@Data
public class DestCurrency implements Serializable {

  private static final long serialVersionUID = 6714222978478249172L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ISO_CODE")
  private String isoCode;

}
