package com.sagag.eshop.repo.entity;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "COUPON_CONDITIONS")
@Entity
@NamedQuery(name = "CouponConditions.findAll", query = "select c from CouponConditions c")
@Data
public class CouponConditions implements Serializable {


  private static final long serialVersionUID = -6432600255055612624L;

  private static final String DILIMITER = ",";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "COUPON_CODE")
  private String couponsCode;

  @Column(name = "DATE_START")
  private Date dateStart;

  @Column(name = "DATE_END")
  private Date dateEnd;

  @Column(name = "USAGE_COUNT")
  private int usageCount;

  @Column(name = "USED_COUNT")
  private int usedCount;

  @Column(name = "AMOUNT")
  private double amout;

  @Column(name = "PERCENTAGE")
  private Integer percentage;

  @Column(name = "DISCOUNT_ARTICLE_ID")
  private long discountArticleId;

  @Column(name = "MINIMUM_ORDER_AMOUNT")
  private double minimumOrderAmount;

  @Column(name = "MAXIMUM_DISCOUNT")
  private Double maximumDiscount;

  @Column(name = "BULK_QUANTITY_TRIGGER")
  private Double bulkQuantityTrigger;

  @Column(name = "BULK_QUANTITY_ARTICLE_ID")
  private Double bulkQuantityArticleId;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "AFFILIATE")
  private String affiliate;

  @Column(name = "CUSTOMER_GROUP")
  private String customerGroup;

  @Column(name = "CUSTOMER_NR")
  private String customerNr;

  @Column(name = "ARTICLE_CATEGORIES")
  private String articleCategories;

  @Column(name = "ARTICLE_ID")
  private String articleId;

  @Column(name = "BRANDS")
  private String brands;

  @Column(name = "UPDATED_BY")
  private String updatedBy;

  @Column(name = "DATE_OF_LAST_UPDATE")
  private Date dateOfLastUpdate;

  @Column(name = "CREATED_BY")
  private String createdBy;

  @Column(name = "DATE_OF_CREATION")
  private Date dateOfCreation;

  @Column(name = "UMAR_ID")
  private long umarId;

  boolean singleCustomer;

  @Transient
  public List<String> getSplitedGenartIds() {
    if (StringUtils.isEmpty(articleCategories)) {
      return Collections.emptyList();
    }
    return Arrays.asList(articleCategories.split(DILIMITER));
  }

  @Transient
  public List<String> getSplitedBrands() {
    if (StringUtils.isEmpty(brands)) {
      return Collections.emptyList();
    }
    return Arrays.asList(brands.split(DILIMITER));
  }

  @Transient
  public List<String> getSplitedArticleIds() {
    if (StringUtils.isEmpty(articleId)) {
      return Collections.emptyList();
    }
    return Arrays.asList(articleId.split(DILIMITER));
  }
}
