package com.sagag.services.tools.domain.target;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity of supported affiliate table.
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "SUPPORTED_AFFILIATE")
public class SupportedAffiliate implements Serializable {

  private static final long serialVersionUID = -3407700580982681455L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "SHORT_NAME")
  private String shortName;

  @Column(name = "COMPANY_NAME")
  private String companyName;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ES_SHORT_NAME")
  private String esShortName;

  @Column(name = "SALES_ORIGIN_ID")
  private String salesOriginId;

  @Column(name = "COUNTRY_ID")
  private Integer countryId;

  @Column(name = "SHOW_PFAND_ARTICLE")
  private Boolean showPfandArticle;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Column(name = "LOGO_LINK")
  private String logoLink;

  @Column(name = "NO_REPLY_EMAIL")
  private String noReplyEmail;
}
