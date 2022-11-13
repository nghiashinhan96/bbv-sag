package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "OrganisationType.findAll", query = "SELECT o FROM OrganisationType o")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "ORGANISATION_TYPE")
public class OrganisationType implements Serializable {

  private static final long serialVersionUID = 467865563649443653L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  private int level;

  private String name;

}
