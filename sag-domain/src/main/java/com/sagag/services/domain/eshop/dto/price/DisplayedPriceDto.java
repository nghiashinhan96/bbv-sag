package com.sagag.services.domain.eshop.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisplayedPriceDto implements Serializable {

  private static final long serialVersionUID = 7578414916187354685L;

  private String type;
  private String brand;
  private Long brandId;
  private Double price;
  private Double totalPrice;
  private Double priceWithVat;
  private Double totalPriceWithVat;

  @JsonIgnore
  public boolean isValidItem() {
    return Objects.nonNull(price) && !price.equals(NumberUtils.DOUBLE_ZERO);
  }

  @JsonIgnore
  public DisplayedPriceBrand toDisplayedPriceBrand() {
    return new DisplayedPriceBrand(brandId, brand);
  }

  @JsonIgnore
  public Double reCalculateTotalPrice(Double quantity) {
    totalPrice = quantity * price;
    return totalPrice;
  }
}
