package com.sagag.services.service.order.history;

import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.hazelcast.domain.order.OrderHistoryFilters;
import com.sagag.services.service.request.ViewOrderHistoryRequestBody;
import com.sagag.services.service.utils.order.OrdersUtils;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

public class AbstractOrderHistoryHandler implements OrderHistoryHandler {

  @Autowired
  private UserService userService;

  @Autowired
  private OrderExternalService orderExternalService;

  @Override
  public OrderHistoryFilters getFilters(final UserInfo user) {
    if (user.isSalesAssistantRole()) {
      throw new IllegalArgumentException("It does not support sale user");
    }
    final List<String> filteredUsernames = new ArrayList<>();
    if (user.isNormalUserRole()) {
      filteredUsernames.add(user.getUsername());
    } else {
      filteredUsernames.add(OrdersUtils.AGGREGRATION_ALL);
      final int orgId = Optional.ofNullable(user.getOrganisationId())
          .orElseThrow(() -> new ValidationException("Can not find organisation information"));
      filteredUsernames.addAll(userService.getAllUsernamesByOrgId(orgId));
    }
    return OrderHistoryFilters.builder().orderStatuses(OrdersUtils.ORDER_STATUSES_FILTER_LIST)
        .usernames(filteredUsernames).build();
  }

  @Override
  public Optional<ExternalOrderHistory> handleExternalOrderHistory(String companyName,
      String customerNr, ViewOrderHistoryRequestBody body, Integer page) {
    final String utcDateFromStr = OrdersUtils.getStrDateFrom(body.getFrom());
    final String utcDateToStr = OrdersUtils.getStrDateTo(body.getTo());
    final ExternalOrderHistoryRequest request = ExternalOrderHistoryRequest.builder()
        .from(utcDateFromStr)
        .to(utcDateToStr).build();
    return orderExternalService.getExternalOrderHistoryOfCustomer(companyName, customerNr, request,
        page);
  }

  @Override
  public Optional<ExternalOrderHistory> handleExternalOrderHistoryDetail(final String orderNumber,
      String companyName, final String custNr) {
    return orderExternalService.getOrderDetailByOrderNr(companyName, custNr, orderNumber);
  }

  @Override
  public Map<String, VCustomerOrderHistory> doFilterOrderHistory(
      Map<String, VCustomerOrderHistory> orderMap,
      Map<String, ExternalOrderDetail> externalOrderHistory,
      ViewOrderHistoryRequestBody condition) {
    // No need to filter by default
    return orderMap;
  }

  @Override
  public Optional<ExternalOrderPositions> handlelExternalOrderPosistionHistory(String orderNr,
      String companyName, String customerNr) {
    return orderExternalService.getOrderPositions(companyName, customerNr, orderNr);
  }

}
