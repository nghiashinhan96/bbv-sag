package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ADDRESS database table.
 * 
 */
@Entity
@Table(name = "ADDRESS")
@NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
@Data
public class Address implements Serializable {

  private static final long serialVersionUID = 8466758630219878919L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ADDRESS_TYPE_ID")
  private Integer addressTypeId;

  private String city;

  private String countryiso;

  private String line1;

  private String line2;

  private String line3;

  private String state;

  private String zipcode;

}
