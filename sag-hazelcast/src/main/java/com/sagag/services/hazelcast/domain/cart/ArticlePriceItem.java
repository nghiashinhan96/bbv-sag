package com.sagag.services.hazelcast.domain.cart;

import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePriceItem implements Serializable {

  private static final long serialVersionUID = 6072322396263387151L;

  private Double grossPrice;
  private Double netPrice;
  private Double vatInPercent;
  private Double originalBrandPrice;
  private Double discountPrice;
  private Double discountInPercent;
  private Double totalGrossPrice;
  private Double totalNetPrice;
  private Double net1Price;
  private Double totalNet1Price;
  private Double promotionInPercent;

  private Double uvpePriceWithVat;
  private Double oepPriceWithVat;
  private Double kbPriceWithVat;
  private Double uvpPriceWithVat;
  private Double dpcPriceWithVat;

  private Double netPriceWithVat;
  private Double grossPriceWithVat;
  private Double totalNetPriceWithVat;
  private Double totalGrossPriceWithVat;
  private Double net1PriceWithVat;
  private Double totalNet1PriceWithVat;

  /**
   * Map PriceWithArticlePrice entity to ArticlePriceItem dto.
   *
   * @param price the PriceWithArticlePrice
   * @param vatRate
   */
  public ArticlePriceItem(PriceWithArticlePrice price, final double vatRate) {
    this.grossPrice = price.defaultGrossPrice();
    this.netPrice = AxPriceUtils.defaultPriceValue(price.getNetPrice());
    this.vatInPercent = vatRate;

    this.originalBrandPrice = AxPriceUtils.defaultPriceValue(price.getOriginalBrandPrice());
    this.discountPrice = AxPriceUtils.defaultPriceValue(price.getDiscountPrice());
    this.discountInPercent = AxPriceUtils.defaultPriceValue(price.getDiscountInPercent());
    this.totalGrossPrice = AxPriceUtils.defaultPriceValue(price.getTotalGrossPrice());
    this.totalNetPrice = AxPriceUtils.defaultPriceValue(price.getTotalNetPrice());
    this.net1Price = AxPriceUtils.defaultPriceValue(price.getNet1Price());
    this.totalNet1Price = AxPriceUtils.defaultPriceValue(price.getTotalNet1Price());
    this.promotionInPercent = AxPriceUtils.defaultPriceValue(price.getPromotionInPercent());

    this.netPriceWithVat = AxPriceUtils.defaultPriceValue(price.getNetPriceWithVat());
    this.grossPriceWithVat = AxPriceUtils.defaultPriceValue(price.getGrossPriceWithVat());
    this.totalNetPriceWithVat = AxPriceUtils.defaultPriceValue(price.getTotalNetPriceWithVat());
    this.totalGrossPriceWithVat = AxPriceUtils.defaultPriceValue(price.getTotalGrossPriceWithVat());
    this.net1PriceWithVat = AxPriceUtils.defaultPriceValue(price.getNet1PriceWithVat());
    this.totalNet1PriceWithVat = AxPriceUtils.defaultPriceValue(price.getTotalNet1PriceWithVat());

    this.uvpePriceWithVat = AxPriceUtils.defaultPriceValue(price.getUvpePriceWithVat());
    this.oepPriceWithVat = AxPriceUtils.defaultPriceValue(price.getOepPriceWithVat());
    this.kbPriceWithVat = AxPriceUtils.defaultPriceValue(price.getKbPriceWithVat());
    this.uvpPriceWithVat = AxPriceUtils.defaultPriceValue(price.getUvpPriceWithVat());
    this.dpcPriceWithVat = AxPriceUtils.defaultPriceValue(price.getDpcPriceWithVat());
  }

}
