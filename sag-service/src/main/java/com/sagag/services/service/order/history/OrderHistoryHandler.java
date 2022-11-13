package com.sagag.services.service.order.history;

import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.hazelcast.domain.order.OrderHistoryFilters;
import com.sagag.services.service.request.ViewOrderHistoryRequestBody;

import java.util.Map;
import java.util.Optional;

public interface OrderHistoryHandler {

  OrderHistoryFilters getFilters(UserInfo user);

  Optional<ExternalOrderHistory> handleExternalOrderHistory(String companyName, String customerNr,
      ViewOrderHistoryRequestBody body, Integer page);

  Optional<ExternalOrderHistory> handleExternalOrderHistoryDetail(String orderNumber,
      String companyName, String custNr);

  Map<String, VCustomerOrderHistory> doFilterOrderHistory(
      Map<String, VCustomerOrderHistory> orderMap,
      Map<String, ExternalOrderDetail> externalOrderHistory, ViewOrderHistoryRequestBody condition);

  Optional<ExternalOrderPositions> handlelExternalOrderPosistionHistory(String orderNr,
      String companyName, String customerNr);

}
