package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "ADDRESS_TYPE")
@Entity
@NamedQuery(name = "AddressType.findAll", query = "SELECT a FROM AddressType a")
@Data
@ToString(exclude = { "addresses" })
public class AddressType implements Serializable {

  private static final long serialVersionUID = -8101098860381140386L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  private String type;

  @OneToMany(mappedBy = "addressType")
  @JsonBackReference
  private List<EshopAddress> addresses;

}
