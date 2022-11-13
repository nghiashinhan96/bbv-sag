package com.sagag.eshop.repo.entity.offer;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(exclude = "properties", callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OFFER_PERSON")
public class OfferPerson implements Serializable {

  private static final long serialVersionUID = 2969618297861749408L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The garage create offer for this person
   */
  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(nullable = false)
  private String type;

  private String status;

  @Column(name = "OFFER_COMPANY_NAME")
  private String offerCompanyName;

  private String firstName;

  // When migrating from old system, set unkown for customer doesn't have last name
  @Column(nullable = false)
  private String lastName;

  private Double hourlyRate;

  private String email;

  @Column(name = "LANGUAGE_ID")
  private Integer languageId;

  private String tecstate;

  private Long createdUserId;

  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  @OneToMany(mappedBy = "person", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
  @JsonManagedReference
  private Set<OfferPersonProperty> properties;

  @OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
  private OfferAddress offerAddress;

  private Integer version;

}
