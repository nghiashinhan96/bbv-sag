package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c")
@Data
public class Country implements Serializable {

  private static final long serialVersionUID = -4992631557467410408L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "SHORT_NAME")
  private String shortName;

}
