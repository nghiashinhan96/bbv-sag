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
 * The persistent class for the SUPPORTED_BRAND_PROMOTION database table.
 * 
 */
@Entity
@Table(name = "SUPPORTED_BRAND_PROMOTION")
@NamedQuery(name = "DestSupportedBrandPromotion.findAll", query = "SELECT s FROM DestSupportedBrandPromotion s")
@Data
public class DestSupportedBrandPromotion implements Serializable {

  private static final long serialVersionUID = -1008826701192046407L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ACTIVE")
  private Boolean active;

  @Column(name = "ARTICLE_SHOP_TYPE")
  private String articleShopType;

  @Column(name = "BRAND")
  private String brand;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "END_DATE")
  private Date endDate;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "START_DATE")
  private Date startDate;

  @Column(name = "SUPPORTED_AFFILIATE_ID")
  private Integer supportedAffiliateId;

}
