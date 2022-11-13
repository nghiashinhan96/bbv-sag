package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.enums.order.OrderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContextDto implements Serializable {

  private static final long serialVersionUID = -7220904547067974816L;

  private String vehicleId;

  private VehicleDto vehicleDoc;

  private String vehSearchTerm;

  private String vehSearchMode;

  private String[] categoryIds;

  private String articleNumber;

  private boolean fromOffer;

  private UserPriceContextDto userPriceOptionContext;

  @JsonProperty("eshop_basket_setting")
  private EshopBasketDto eshopBasketDto;

  public boolean isCounterBasketMode() {
    return Objects.nonNull(eshopBasketDto) && !StringUtils.isEmpty(eshopBasketDto.getOrderType())
        && OrderType.COUNTER.name().equals(eshopBasketDto.getOrderType());
  }
}
