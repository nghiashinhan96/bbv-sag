package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "COUPON_USE_LOG")
@Entity
@NamedQuery(name = "DestCouponUseLog.findAll", query = "select c from DestCouponUseLog c")
@Data
public class DestCouponUseLog implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "AFFILIATE_MATCH")
  private String affiliateMatch;

  @Column(name = "AMOUNT_APPLIED")
  private Double amoutApplied;

  @Column(name = "ARTICLE_CATEGORIES")
  private String articleCategories;

  @Column(name = "ARTICLE_ID_MATCH")
  private String articleIdMatch;

  @Column(name = "BRANDS_MATCH")
  private String brandsMatch;

  @Column(name = "COUNTRY_MATCH")
  private String countryMatch;

  @Column(name = "COUPONS_CODE")
  private String couponsCode;

  @Column(name = "CUSTOMER_NR")
  private String customerNr;

  @Column(name = "DATE_USED")
  private Date dateUsed;

  @Column(name = "DISCOUNT_ARTICLE_ID")
  private Long discountArticleId;

  @Column(name = "ORDER_CONFIRMATION_ID")
  private Long orderConfirmationId;

  @Column(name = "ORDER_ID")
  private Long orderID;

  @Column(name = "UMAR_ID")
  private Long umarId;

  @Column(name = "USAGE_COUNT_REMAINDER")
  private Integer usageCountRemainder;

  @Column(name = "USER_ID")
  private String userId;

}
