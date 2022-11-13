package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "Branch.findAll", query = "SELECT b FROM Branch b")
@Data
@Builder
@Table(name = "BRANCH")
@NoArgsConstructor
@AllArgsConstructor
public class Branch implements Serializable {

  private static final long serialVersionUID = 6353100466100810921L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "BRANCH_NUMBER", nullable = false)
  private Integer branchNr;

  @Column(name = "BRANCH_CODE")
  private String branchCode;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "ZIP")
  private String zip;

  @Column(name = "ADDRESS_STREET")
  private String addressStreet;

  @Column(name = "ADDRESS_CITY")
  private String addressCity;

  @Column(name = "ADDRESS_DESC")
  private String addressDesc;

  @Column(name = "ADDRESS_COUNTRY")
  private String addressCountry;

  @Column(name = "COUNTRY_ID")
  private Integer countryId;

  @Column(name = "REGION_ID")
  private String regionId;

  @Column(name = "PRIMARY_FAX")
  private String primaryFax;

  @Column(name = "PRIMARY_EMAIL")
  private String primaryEmail;

  @Column(name = "PRIMARY_PHONE")
  private String primaryPhone;

  @Column(name = "PRIMARY_URL")
  private String primaryUrl;

  @Column(name = "ID_KSL", nullable = false)
  private Boolean validForKSL;

  @Column(name = "OPENING_TIME", nullable = false)
  @Deprecated
  private Time openingTime;

  @Column(name = "CLOSING_TIME", nullable = false)
  @Deprecated
  private Time closingTime;

  @Column(name = "LUNCH_START_TIME")
  @Deprecated
  private Time lunchStartTime;

  @Column(name = "LUNCH_END_TIME")
  @Deprecated
  private Time lunchEndTime;

  @Column(name = "HIDE_FROM_CUSTOMERS", nullable = false)
  private Boolean hideFromCustomers;

  @Column(name = "HIDE_FROM_SALES", nullable = false)
  private Boolean hideFromSales;

  public Branch(Integer branchNr, String branchCode) {
    this.branchNr = branchNr;
    this.branchCode = branchCode;
  }

  public Branch(Integer branchNr, Boolean hideFromCustomers, Boolean hideFromSales) {
    this.branchNr = branchNr;
    this.hideFromCustomers = hideFromCustomers;
    this.hideFromSales = hideFromSales;
  }

}
