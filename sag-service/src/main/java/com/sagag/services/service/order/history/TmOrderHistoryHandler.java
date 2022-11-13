package com.sagag.services.service.order.history;

import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.SoapSendOrderExternalService;
import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.common.profiles.TopmotiveErpProfile;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.hazelcast.domain.order.OrderHistoryFilters;
import com.sagag.services.service.request.ViewOrderHistoryRequestBody;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@TopmotiveErpProfile
public class TmOrderHistoryHandler extends AbstractOrderHistoryHandler {

  @Autowired
  private SoapSendOrderExternalService sendOrderExtService;

  @Override
  public OrderHistoryFilters getFilters(UserInfo user) {
    if (user.isSalesAssistantRole()) {
      throw new IllegalArgumentException("It does not support sale user");
    }
    return OrderHistoryFilters.builder().build();
  }

  @Override
  public Optional<ExternalOrderHistory> handleExternalOrderHistory(String companyName,
      String customerNr, ViewOrderHistoryRequestBody searchCondition, Integer page) {
    ExternalOrderHistoryRequest orderHistoryRequest =
        SagBeanUtils.map(searchCondition, ExternalOrderHistoryRequest.class);
    return sendOrderExtService.getExternalOrderHistoryOfCustomer(companyName, customerNr,
        orderHistoryRequest, page);
  }

  @Override
  public Optional<ExternalOrderHistory> handleExternalOrderHistoryDetail(String orderNumber,
      String companyName, String custNr) {
    Integer firstPage = 1;
    return sendOrderExtService.getExternalOrderHistoryOfCustomer(companyName, custNr,
        ExternalOrderHistoryRequest.builder().orderNumber(orderNumber).build(), firstPage);
  }

  @Override
  public Map<String, VCustomerOrderHistory> doFilterOrderHistory(
      Map<String, VCustomerOrderHistory> orderMap,
      Map<String, ExternalOrderDetail> externalOrderHistory,
      ViewOrderHistoryRequestBody condition) {
    return MapUtils.emptyIfNull(orderMap).entrySet().stream()
        .filter(order -> StringUtils.isBlank(condition.getOrderNumber())
            || StringUtils.equals(order.getKey(), condition.getOrderNumber()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public Optional<ExternalOrderPositions> handlelExternalOrderPosistionHistory(String orderNr,
      String companyName, String customerNr) {
    return sendOrderExtService.getExternalOrderPosistion(orderNr, companyName, customerNr);
  }

}
