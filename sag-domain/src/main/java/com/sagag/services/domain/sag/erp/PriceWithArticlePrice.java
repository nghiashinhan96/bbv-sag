package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * POJO class of price with article price.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceWithArticlePrice implements Serializable {

  private static final long serialVersionUID = -1393453446377578425L;

  private Double recommendedRetailPrice;

  private Double originalBrandPrice;

  private Double recommendedSellPrice;

  private Double grossPrice;

  private Double netPrice;

  private Double discountPrice;

  private Double discountInPercent;

  private Double vatInPercent;

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

  private Double uvpePrice;

  private Double oepPrice;

  private Double kbPrice;

  private Double uvpPrice;

  private Double uvpePriceWithVat;

  private Double oepPriceWithVat;

  private Double kbPriceWithVat;

  private Double uvpPriceWithVat;

  private Double standardGrossPrice;

  private Double standardGrossPriceWithVat;

  private String type;

  private String currency;

  private Double grossPriceWithVat;

  private Double totalGrossPriceWithVat;

  private Double netPriceWithVat;

  private Double totalNetPriceWithVat;

  private Double promotionInPercent;

  private Boolean net1PriceFound;

  private Double net1Price;

  private Double net1PriceWithVat;

  private Double totalNet1Price;

  private Double totalNet1PriceWithVat;

  private Double finalCustomerNetPrice;

  private Double dpcPrice;

  private Double dpcPriceWithVat;

  public Double defaultGrossPrice() {
    return getGrossPrice() != null ? getGrossPrice() : 0d;
  }

  public Double defaultNetPrice() {
    return getNetPrice() != null ? getNetPrice() : 0d;
  }
}
