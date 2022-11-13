package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the OFFER_ADDRESS database table.
 * 
 */
@Entity
@Table(name = "OFFER_ADDRESS")
@NamedQuery(name = "DestOfferAddress.findAll", query = "SELECT o FROM DestOfferAddress o")
@Data
public class DestOfferAddress implements Serializable {

  private static final long serialVersionUID = -3351712596849872749L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "CITY")
  private String city;

  @Column(name = "COUNTRYISO")
  private String countryiso;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "ERP_ID")
  private String erpId;

  @Column(name = "LINE1")
  private String line1;

  @Column(name = "LINE2")
  private String line2;

  @Column(name = "LINE3")
  private String line3;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "PO_BOX")
  private String poBox;

  @Column(name = "[STATE]")
  private String state;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "[TYPE]")
  private String type;

  @Column(name = "[VERSION]")
  private Integer version;

  @Column(name = "ZIPCODE")
  private String zipcode;

}
