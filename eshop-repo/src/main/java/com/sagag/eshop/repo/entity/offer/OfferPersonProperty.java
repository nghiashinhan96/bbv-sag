package com.sagag.eshop.repo.entity.offer;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OFFER_PERSON_PROPERTY")
@IdClass(OfferPersonPropertyId.class)
public class OfferPersonProperty implements Serializable {

  private static final long serialVersionUID = -4231416284314961211L;

  @Id
  @Column(name = "PERSON_ID")
  private Long personId;

  @ManyToOne
  @JoinColumn(name = "PERSON_ID", insertable = false, updatable = false)
  @JsonBackReference
  private OfferPerson person;

  @Id
  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String value;

}
