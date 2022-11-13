package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the OFFER_PERSON database table.
 * 
 */
@Entity
@Table(name = "OFFER_PERSON")
@NamedQuery(name = "OfferPerson.findAll", query = "SELECT o FROM OfferPerson o")
@Data
public class OfferPerson implements Serializable {

  private static final long serialVersionUID = -1234202223963540307L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "HOURLY_RATE")
  private BigDecimal hourlyRate;

  @Column(name = "LANGUAGE_ID")
  private Integer languageId;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "OFFER_COMPANY_NAME")
  private String offerCompanyName;

  @Column(name = "ORGANISATION_ID")
  private Integer organisationId;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "[VERSION]")
  private Integer version;

}
