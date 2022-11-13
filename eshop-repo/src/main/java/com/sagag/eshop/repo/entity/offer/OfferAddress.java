package com.sagag.eshop.repo.entity.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(exclude = { "person" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OFFER_ADDRESS")
public class OfferAddress implements Serializable {

  private static final long serialVersionUID = -47703626938352167L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String city;

  @Column(nullable = false)
  private String countryiso;

  @Column(name = "PERSON_ID", nullable = false)
  private Long personId;

  @OneToOne
  @JoinColumn(name = "PERSON_ID", referencedColumnName = "ID", insertable = false,
      updatable = false)
  private OfferPerson person;

  private String line1;

  private String line2;

  private String line3;

  private String state;

  private String zipcode;

  @Column(nullable = false)
  private String tecstate;

  private String erpId;

  private String poBox;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private Long createdUserId;

  @Column(nullable = false)
  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  @Column(nullable = false)
  private Integer version;
}
