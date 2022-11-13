package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the BRANCH database table.
 * 
 */
@Entity
@Table(name = "BRANCH")
@NamedQuery(name = "DestBranch.findAll", query = "SELECT b FROM DestBranch b")
@Data
public class DestBranch implements Serializable {

  private static final long serialVersionUID = 8390052403280326687L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ADDRESS_CITY")
  private String addressCity;

  @Column(name = "ADDRESS_COUNTRY")
  private String addressCountry;

  @Column(name = "ADDRESS_DESC")
  private String addressDesc;

  @Column(name = "ADDRESS_STREET")
  private String addressStreet;

  @Column(name = "BRANCH_CODE")
  private String branchCode;

  @Column(name = "BRANCH_NUMBER")
  private Integer branchNumber;

  @Column(name = "CLOSING_TIME")
  private String closingTime;

  @Column(name = "COUNTRY_ID")
  private Integer countryId;

  @Column(name = "ID_KSL")
  private Boolean idKsl;

  @Column(name = "LUNCH_END_TIME")
  private String lunchEndTime;

  @Column(name = "LUNCH_START_TIME")
  private String lunchStartTime;

  @Column(name = "OPENING_TIME")
  private String openingTime;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "PRIMARY_EMAIL")
  private String primaryEmail;

  @Column(name = "PRIMARY_FAX")
  private String primaryFax;

  @Column(name = "PRIMARY_PHONE")
  private String primaryPhone;

  @Column(name = "PRIMARY_URL")
  private String primaryUrl;

  @Column(name = "REGION_ID")
  private String regionId;

  @Column(name = "ZIP")
  private String zip;

}
