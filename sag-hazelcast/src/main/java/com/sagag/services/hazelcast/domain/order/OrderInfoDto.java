package com.sagag.services.hazelcast.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDto {

  private String invoiceTypeCode;

  private String reference;

  private String branchRemark;

  private List<OrderItemDetailDto> items;

  private Double totalPrice;

  private Boolean showPriceType;

  public static OrderInfoDto createOrderInfoDtoFromJson(String json) {
    return SagJSONUtil.jsonToObject(json, OrderInfoDto.class);
  }

  @JsonIgnore
  public List<String> getVehicleInfosFromOrderItem() {
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyList();
    }
    return items.stream()
            .filter(item -> StringUtils.isNotBlank(item.getVehicleInfo()))
            .map(OrderItemDetailDto::getVehicleInfo).collect(Collectors.toList());
  }
}
