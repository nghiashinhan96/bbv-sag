package com.sagag.eshop.repo.entity.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ORGANISATION_COLLECTION")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationCollection implements Serializable {
  private static final long serialVersionUID = -7018144848443104165L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String name;

  @Column(name = "SHORTNAME")
  private String shortname;

  @Column(name = "AFFILIATE_ID")
  private int affiliateId;

  private String description;
}
