package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "Currency.findAll", query = "SELECT c FROM Currency c")
@Data
public class Currency implements Serializable {

  private static final long serialVersionUID = -5663325555942990206L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ISO_CODE")
  private String isoCode;

}
