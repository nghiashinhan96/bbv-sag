package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the COUPON_CONDITIONS database table.
 * 
 */
@Entity
@Table(name = "COUPON_CONDITIONS")
@NamedQuery(name = "CouponConditions.findAll", query = "SELECT c FROM CouponConditions c")
@Data
public class CouponConditions implements Serializable {

  private static final long serialVersionUID = 1659588361401344254L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "AFFILIATE")
  private String affiliate;

  @Column(name = "AMOUNT")
  private BigDecimal amount;

  @Column(name = "ARTICLE_CATEGORIES")
  private String articleCategories;

  @Column(name = "ARTICLE_ID")
  private String articleId;

  @Column(name = "BRANDS")
  private String brands;

  @Column(name = "BULK_QUANTITY_ARTICLE_ID")
  private Long bulkQuantityArticleId;

  @Column(name = "BULK_QUANTITY_TRIGGER")
  private Integer bulkQuantityTrigger;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "COUPON_CODE")
  private String couponCode;

  @Column(name = "CREATED_BY")
  private String createdBy;

  @Column(name = "CUSTOMER_GROUP")
  private String customerGroup;

  @Column(name = "CUSTOMER_NR")
  private Long customerNr;

  @Column(name = "DATE_END")
  private Date dateEnd;

  @Column(name = "DATE_OF_CREATION")
  private Date dateOfCreation;

  @Column(name = "DATE_OF_LAST_UPDATE")
  private Date dateOfLastUpdate;

  @Column(name = "DATE_START")
  private Date dateStart;

  @Column(name = "DISCOUNT_ARTICLE_ID")
  private Long discountArticleId;

  @Column(name = "MAXIMUM_DISCOUNT")
  private BigDecimal maximumDiscount;

  @Column(name = "MINIMUM_ORDER_AMOUNT")
  private BigDecimal minimumOrderAmount;

  @Column(name = "PERCENTAGE")
  private Integer percentage;

  @Column(name = "SINGLE_CUSTOMER")
  private boolean singleCustomer;

  @Column(name = "UMAR_ID")
  private Long umarId;

  @Column(name = "UPDATED_BY")
  private String updatedBy;

  @Column(name = "USAGE_COUNT")
  private Integer usageCount;

  @Column(name = "USED_COUNT")
  private Integer usedCount;

}