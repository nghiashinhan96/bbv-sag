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
 * The persistent class for the SUPPORTED_AFFILIATE database table.
 * 
 */
@Entity
@Table(name = "SUPPORTED_AFFILIATE")
@NamedQuery(name = "DestSupportedAffiliate.findAll", query = "SELECT s FROM DestSupportedAffiliate s")
@Data
public class DestSupportedAffiliate implements Serializable {

  private static final long serialVersionUID = -6239116923043602475L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "COMPANY_NAME")
  private String companyName;

  @Column(name = "COUNTRY_ID")
  private Integer countryId;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ES_SHORT_NAME")
  private String esShortName;

  @Column(name = "LOGO_LINK")
  private String logoLink;

  @Column(name = "NO_REPLY_EMAIL")
  private String noReplyEmail;

  @Column(name = "SALES_ORIGIN_ID")
  private String salesOriginId;

  @Column(name = "SHORT_NAME")
  private String shortName;

  @Column(name = "SHOW_PFAND_ARTICLE")
  private Boolean showPfandArticle;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

}
