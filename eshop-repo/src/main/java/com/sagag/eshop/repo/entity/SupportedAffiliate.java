package com.sagag.eshop.repo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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

  private String shortName;

  private String companyName;

  private String description;

  private String esShortName;

  private String salesOriginId;

  private Integer countryId;

  private Boolean showPfandArticle;

  private Date updatedDate;

  private String logoLink;

  private String noReplyEmail;

  private String startWorkingTime;

  private String endWorkingTime;
}
