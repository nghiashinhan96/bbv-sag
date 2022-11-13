package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.ax.enums.AxPriceType;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import lombok.Data;

/**
 * Class to receive the article price from Dynamic AX ERP.
 *
 */
@Data
@JsonPropertyOrder(
    {
      "articleId",
      "currency",
      "grossPrice",
      "netPrice",
      "discountPrice",
      "totalGrossPrice",
      "totalNetPrice",
      "totalDiscountPrice",
      "discountInPercent",
      "net1Price",
      "net1PriceFound",
      "totalNet1Price"
    })
public class AxPriceWithArticle implements Serializable {

  private static final long serialVersionUID = 8595011431758237193L;

  private String articleId;

  private String currency;

  private Double grossPrice;

  private Double netPrice;

  private Double discountPrice;

  private Double totalGrossPrice;

  private Double totalNetPrice;

  private Double totalDiscountPrice;

  private Double discountInPercent;

  private String errorMessage;

  private boolean priceFound;

  private Double net1Price;

  private Double totalNet1Price;

  private boolean net1PriceFound;

  private List<AxAdditionalPrice> additionalPrices;

  @JsonIgnore
  public PriceWithArticle toPriceDto() {
    return PriceWithArticle.builder().articleId(this.articleId)
        .price(toPriceWithArticlePriceDto()).build();
  }

  private PriceWithArticlePrice toPriceWithArticlePriceDto() {
    // #2679: Process get uvpePrice and oepPrice to update the article price info
    final AxAdditionalPrice axUvpePrice =
        getAdditionalPrice(additionalPrices, AxPriceType.UVPE);
    final AxAdditionalPrice axOepPrice =
        getAdditionalPrice(additionalPrices, AxPriceType.OEP);
    final AxAdditionalPrice axUvpPrice =
        getAdditionalPrice(additionalPrices, AxPriceType.UVP);
    final AxAdditionalPrice axKbPrice =
        getAdditionalPrice(additionalPrices, AxPriceType.KB);
    final AxAdditionalPrice axDpcPrice =
        getAdditionalPrice(additionalPrices, AxPriceType.DPC);
    return PriceWithArticlePrice.builder()
        .grossPrice(this.grossPrice)
        .netPrice(this.netPrice).discountPrice(this.discountPrice)
        .totalNetPrice(this.totalNetPrice)
        .totalNet1Price(this.totalNet1Price)
        .totalGrossPrice(this.totalGrossPrice)
        .totalDiscountPrice(this.totalDiscountPrice)
        .discountInPercent(this.discountInPercent)
        .uvpePrice(Objects.isNull(axUvpePrice) ? null : axUvpePrice.getPriceValue())
        .oepPrice(Objects.isNull(axOepPrice) ? null : axOepPrice.getPriceValue())
        .kbPrice(Objects.isNull(axKbPrice) ? null : axKbPrice.getPriceValue())
        .uvpPrice(Objects.isNull(axUvpPrice) ? null : axUvpPrice.getPriceValue())
        .dpcPrice(Objects.isNull(axDpcPrice) ? null : axDpcPrice.getPriceValue())
        .net1PriceFound(this.net1PriceFound)
        .net1Price(this.net1Price)
        .build();
  }

  private static AxAdditionalPrice getAdditionalPrice(
      final List<AxAdditionalPrice> additionalPrices, final AxPriceType type) {
    if (CollectionUtils.isEmpty(additionalPrices) || Objects.isNull(type)) {
      return null;
    }
    return additionalPrices.stream().filter(filterByType(type))
        .findFirst().orElse(null);
  }

  private static Predicate<AxAdditionalPrice> filterByType(AxPriceType type) {
    return price -> {
      String priceType = StringUtils.substringAfterLast(price.getPriceType(), SagConstants.UNDERSCORE);
      return priceType.equalsIgnoreCase(type.name());
    };
  }

}
