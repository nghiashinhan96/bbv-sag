package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "AddressType.findAll", query = "SELECT a FROM AddressType a")
@Data
public class AddressType implements Serializable {

  private static final long serialVersionUID = -2324342147182615347L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[TYPE]")
  private String type;

}
