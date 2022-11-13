package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the COUPON_USE_LOG database table.
 * 
 */
@Entity
@Table(name = "COUPON_USE_LOG")
@NamedQuery(name = "CouponUseLog.findAll", query = "select c from CouponUseLog c")
@Data
public class CouponUseLog implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "COUPONS_CODE")
  private String couponsCode;

  @Column(name = "DATE_USED")
  private Date dateUsed;

  @Column(name = "USAGE_COUNT_REMAINDER")
  private Integer usageCountRemainder;

  @Column(name = "AMOUNT_APPLIED")
  private Double amoutApplied;

  @Column(name = "DISCOUNT_ARTICLE_ID")
  private Long discountArticleId;

  @Column(name = "CUSTOMER_NR")
  private String customerNr;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "ORDER_CONFIRMATION_ID")
  private Long orderConfirmationId;

  @Column(name = "ORDER_ID")
  private Long orderID;

  @Column(name = "COUNTRY_MATCH")
  private String countryMatch;

  @Column(name = "AFFILIATE_MATCH")
  private String affiliateMatch;

  @Column(name = "ARTICLE_CATEGORIES")
  private String articleCategories;

  @Column(name = "ARTICLE_ID_MATCH")
  private String articleIdMatch;

  @Column(name = "BRANDS_MATCH")
  private String brandsMatch;

  @Column(name = "UMAR_ID")
  private Long umarId;

}
