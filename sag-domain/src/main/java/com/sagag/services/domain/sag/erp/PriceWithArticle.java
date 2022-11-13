package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sagag.services.common.utils.SagPriceUtils;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * POJO class of price with article.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceWithArticle implements Serializable {

  private static final long serialVersionUID = 8595011431758237193L;

  @JsonSerialize(using = ToStringSerializer.class)
  private String articleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long umarId;

  private PriceWithArticlePrice price;

  public static PriceWithArticle empty() {
    final PriceWithArticle priceWithArticle = new PriceWithArticle();
    priceWithArticle.setPrice(new PriceWithArticlePrice());
    return priceWithArticle;
  }

  public boolean hasNetPrice() {
    return price != null && price.getNetPrice() != null
      && price.getNetPrice().doubleValue() != NumberUtils.DOUBLE_ZERO.doubleValue();
  }

  public boolean hasNet1Price() {
    return !Objects.isNull(price) && !Objects.isNull(price.getNet1Price())
        && price.getNet1PriceFound();
  }

  @JsonIgnore
  public Double getUvpePrice() {
    if (price == null) {
      return null;
    }
    return price.getUvpePrice();
  }

  @JsonIgnore
  public Double getOepPrice() {
    return Optional.ofNullable(price).map(PriceWithArticlePrice::getOepPrice).orElse(null);
  }

  @JsonIgnore
  public Double getGrossPrice() {
    return Optional.ofNullable(price).map(PriceWithArticlePrice::getGrossPrice).orElse(null);
  }

  @JsonIgnore
  public Double getFinalCustomerNetPrice() {
    return Optional.ofNullable(price).map(PriceWithArticlePrice::getFinalCustomerNetPrice)
        .orElse(null);
  }

  @JsonIgnore
  public Double getFinalCustomerNetPriceWithVat() {
    final Double finalCustomerNetPrice = getFinalCustomerNetPrice();
    if (Objects.isNull(finalCustomerNetPrice) || Objects.isNull(price.getVatInPercent())) {
      return null;
    }
    return SagPriceUtils.calculateVATPrice(finalCustomerNetPrice, price.getVatInPercent());
  }

  @JsonIgnore
  public Double getDpcPrice() {
    return Optional.ofNullable(price).map(PriceWithArticlePrice::getDpcPrice).orElse(null);
  }
}
