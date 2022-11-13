package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "ADDRESS")
@Entity
@NamedQuery(name = "EshopAddress.findAll", query = "SELECT a FROM EshopAddress a")
@Data
@ToString(exclude = { "organisationAddresses", "addressType" })
public class EshopAddress implements Serializable {

  private static final long serialVersionUID = 2369736726021677553L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String city;

  private String countryiso;

  private String line1;

  private String line2;

  private String line3;

  private String state;

  private String zipcode;

  @ManyToOne
  @JoinColumn(name = "ADDRESS_TYPE_ID")
  @JsonManagedReference
  private AddressType addressType;

  @OneToMany(mappedBy = "address")
  @JsonBackReference
  private List<OrganisationAddress> organisationAddresses;

}
