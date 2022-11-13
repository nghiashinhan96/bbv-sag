package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ORGANISATION_ADDRESS")
@NamedQuery(name = "OrganisationAddress.findAll", query = "SELECT o FROM OrganisationAddress o")
@Data
@ToString(of = { "id" })
public class OrganisationAddress implements Serializable {

  private static final long serialVersionUID = 5132990583034090861L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JsonManagedReference
  private EshopAddress address;

  @ManyToOne
  @JsonBackReference
  private Organisation organisation;

}
