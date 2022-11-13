package com.sagag.services.tools.domain.elasticsearch;

import lombok.Data;

import java.io.Serializable;

@Data
public class PriceWithArticlePrice implements Serializable {

  private static final long serialVersionUID = -1393453446377578425L;

  private Double grossPrice;
  private Double recommendedRetailPrice;
  private Double originalBrandPrice;
  private Double recommendedSellPrice;
  private Double netPrice;
  private Double discountPrice;
  private Integer discountInPercent;
  private Integer vatInPercent;
  private Double totalGrossPrice;
  private Double totalNetPrice;
  private Double totalDiscountPrice;
  private Double retailPrice;
  private Double quoteBasePriceWithVat;
  private Double totalRecyclingFee;
  private Double totalShippingFee;
  private Double totalAssemblingFee;
  private Boolean showDiscount;
  private Boolean tyrePromotion;
  private Boolean snpArticleGroup;
  private Boolean originalEquipmentPrice;

}
