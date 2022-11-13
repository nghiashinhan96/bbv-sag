package com.sagag.eshop.repo.entity;

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

@Entity
@Table(name = "ORGANISATION_PROPERTY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationProperty implements Serializable {

  private static final long serialVersionUID = 8344567479582711013L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long organisationId;

  @Column(nullable = true)
  private String type;

  @Column(nullable = false)
  private String value;

}
