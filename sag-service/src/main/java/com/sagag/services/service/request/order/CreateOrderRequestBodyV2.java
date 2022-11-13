package com.sagag.services.service.request.order;

import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerDto;
import com.sagag.services.common.contants.SagConstants;

import lombok.Data;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Data
public class CreateOrderRequestBodyV2 implements Serializable {

  private static final long serialVersionUID = -8492163425795797695L;

  private OrderConditionContextBody orderCondition;

  private List<OrderConditionContextBody> orderConditionByLocation;

  private List<OrderItem> items;

  private String personalNumber;

  private String customerRefText;

  private String message;

  private String timezone;

  private String orderFrom;

  private FinalCustomerDto finalCustomer;

  private Long finalCustomerOrderId;

  private String requestDateTime;

  public Map<String, String> getAdditionalTextDocs() {
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyMap();
    }
    final Map<String, String> additionalTextDocs = new HashMap<>();
    String finalCustomerText =
        Optional.ofNullable(finalCustomer).map(FinalCustomerDto::getName).orElse(StringUtils.EMPTY);
    items.stream().forEach(item -> {
      String text = getTextReferenz(finalCustomerText, item.getAdditionalTextDoc());
      if (StringUtils.isNoneBlank(text)) {
        additionalTextDocs.putIfAbsent(item.getCartKey(), text);
      }
    });

    return additionalTextDocs;
  }

  private String getTextReferenz(String finalCustomerText, String additionalText) {
    if (StringUtils.isBlank(finalCustomerText)) {
      return additionalText;
    }
    if (StringUtils.isNoneBlank(additionalText)) {
      return finalCustomerText + SagConstants.COLON + StringUtils.SPACE + additionalText;
    }
    return finalCustomerText;
  }

  public boolean isCompleteDelivery() {
    return Objects.nonNull(orderCondition) && !orderCondition.isPartialDelivery();
  }
}
