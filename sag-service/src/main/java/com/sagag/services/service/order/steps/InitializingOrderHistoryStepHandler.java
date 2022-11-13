package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.order.EshopOrderHistoryState;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.hazelcast.domain.cart.CartItemType;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.order.OrderInfoDto;
import com.sagag.services.hazelcast.domain.order.OrderItemDetailDto;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class InitializingOrderHistoryStepHandler {

  private static final int VEHICLE_INFO_MAX_LENGTH = 255;

  @Autowired
  private OrderHistoryRepository orderHistoryRepo;

  @Autowired
  private UserService userService;

  @Autowired
  private OrderHistoryService orderHistoryService;

  @Autowired
  private MessageSource messageSource;

  public OrderHistory handle(final UserInfo user, final AbstractOrderProcessor processor,
      final ShoppingCart shoppingCart, final Map<String, String> additionalTextDocMap,
      final String invoiceTypeCode, final ExternalOrderRequest orderRequest, final Double total) {

    if (user.isFinalUserRole()) {
      return new OrderHistory();
    }

    updateDuplicatedOrderMessage(user, orderRequest);
    Assert.notEmpty(orderRequest.getBasketPositions(), "The basket is empty");

    final String orderInfoJson = createOrderInfoJson(shoppingCart, additionalTextDocMap,
        invoiceTypeCode, orderRequest, total, user.getSettings().isPriceDisplayChanged());

    // #6753 Add missing info for VIN package
    List<String> vhcInfo = new ArrayList<>();
    shoppingCart.getItems().forEach(
        item -> {
          if (CartItemType.VIN.equals(item.getItemType()) && Objects.nonNull(item.getArticle())
              && Objects.nonNull(item.getArticle().getArticle())) {
            vhcInfo.add(item.getArticle().getArticle().getKeyword());
          } else if (item.hasVehicle()) {
            vhcInfo.add(item.getVehicle().getVehicleInfo().trim());
          }
        }
    );
    final String vehicleInfos =
        vhcInfo.stream().distinct().collect(Collectors.joining(SagConstants.SEMICOLON));

    return storeOrderHistory(processor, user.getId(), user.getCustNrStr(), orderInfoJson,
        user.getSalesId(), total, vehicleInfos);
  }

  public String createOrderInfoJson(ShoppingCart shoppingCart,
      Map<String, String> additionalTextDocMap, String invoiceTypeCode,
      final ExternalOrderRequest orderRequest, Double totalPrice, boolean showPriceType) {

    final List<OrderItemDetailDto> items = shoppingCart.getItems().stream()
        .map(item -> OrderItemDetailDto.createOrderItemDetail(item, additionalTextDocMap))
        .collect(Collectors.toList());

    final OrderInfoDto orderInfoDto = OrderInfoDto.builder()
        .items(items)
        .invoiceTypeCode(invoiceTypeCode)
        .branchRemark(orderRequest.getMessage())
        .reference(orderRequest.getCustomerRefText())
        .totalPrice(totalPrice)
        .showPriceType(showPriceType)
        .build();

    return SagJSONUtil.convertObjectToJson(orderInfoDto);
  }


  private OrderHistory storeOrderHistory(AbstractOrderProcessor processor, final Long userId,
      final String customer, final String orderInfoJson, Long saleId, final Double total,
      final String vehicleInfos) {

    final String truncatedVechicleInfo = StringUtils.isEmpty(vehicleInfos) ? StringUtils.EMPTY
        : vehicleInfos.substring(0, Math.min(VEHICLE_INFO_MAX_LENGTH, vehicleInfos.length()));

    final OrderHistory orderHistory =
        OrderHistory.builder()
            .userId(userId)
            .createdDate(Calendar.getInstance().getTime())
            .orderState(EshopOrderHistoryState.OPEN.name())
            .saleId(saleId)
            .saleName(userService.getFullName(saleId))
            .total(Objects.isNull(total) ? 0F : total.floatValue())
            .customerNumber(Long.valueOf(customer))
            .orderInfoJson(orderInfoJson)
            .type(processor.orderHistoryType().name())
            .vehicleInfos(truncatedVechicleInfo)
            .build();
    return orderHistoryRepo.save(orderHistory);
  }

  private void updateDuplicatedOrderMessage(UserInfo user, ExternalOrderRequest axOrderRequest) {
    String orderState = orderHistoryService.findLatestOrderStateByUserId(user.getId());
    if (!EshopOrderHistoryState.ERP_TIMEOUT_ERROR.name().equals(orderState)) {
      return;
    }
    final Locale locale = user.getUserLocale();
    axOrderRequest.setMessage(messageSource.getMessage("order.dupplicated_messsage", null, locale));
  }

}
