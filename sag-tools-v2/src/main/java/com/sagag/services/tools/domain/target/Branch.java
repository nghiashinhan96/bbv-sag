package com.sagag.services.tools.domain.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Time;

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

  @Column(name = "BRANCH_CODE", nullable = false)
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

  @ManyToOne
  @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
  private Country country;

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
  private Time openingTime;

  @Column(name = "CLOSING_TIME", nullable = false)
  private Time closingTime;

  @Column(name = "LUNCH_START_TIME")
  private Time lunchStartTime;

  @Column(name = "LUNCH_END_TIME")
  private Time lunchEndTime;
}
