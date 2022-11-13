package com.sagag.eshop.repo.entity;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "COUPON_USE_LOG")
@Entity
@NamedQuery(name = "CouponUseLog.findAll", query = "select c from CouponUseLog c")
@Data
public class CouponUseLog implements Serializable {


  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "COUPONS_CODE")
  private String couponsCode;

  @Column(name = "DATE_USED")
  private Date dateUsed;

  @Column(name = "USAGE_COUNT_REMAINDER")
  private int usageCountRemainder;

  @Column(name = "AMOUNT_APPLIED")
  private double amoutApplied;

  @Column(name = "DISCOUNT_ARTICLE_ID")
  private long discountArticleId;

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
  String articleIdMatch;

  @Column(name = "BRANDS_MATCH")
  private String brandsMatch;

  @Column(name = "UMAR_ID")
  private long umarId;

  /**
   * Checks if the coupon has its code.
   *
   * @return <code>true</code> if the coupon has the code, <code>false</code> otherwise.
   */
  @Transient
  public boolean hasCouponCode() {
    return !StringUtils.isBlank(this.couponsCode);
  }

  @Transient
  public boolean isValidCoupon() {
    return hasCouponCode() && discountArticleId != 0;
  }
}
