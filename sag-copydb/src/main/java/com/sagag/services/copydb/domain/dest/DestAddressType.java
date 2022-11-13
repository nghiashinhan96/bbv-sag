package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ADDRESS_TYPE database table.
 * 
 */
@Entity
@Table(name = "ADDRESS_TYPE")
@NamedQuery(name = "DestAddressType.findAll", query = "SELECT a FROM DestAddressType a")
@Data
public class DestAddressType implements Serializable {

  private static final long serialVersionUID = 3566874800979810925L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[TYPE]")
  private String type;

}
