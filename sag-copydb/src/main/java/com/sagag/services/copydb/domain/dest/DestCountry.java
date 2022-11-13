package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the COUNTRY database table.
 * 
 */
@Entity
@Table(name = "COUNTRY")
@NamedQuery(name = "DestCountry.findAll", query = "SELECT c FROM DestCountry c")
@Data
public class DestCountry implements Serializable {

  private static final long serialVersionUID = 1033236979714990232L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "SHORT_CODE")
  private String shortCode;

  @Column(name = "SHORT_NAME")
  private String shortName;

}
