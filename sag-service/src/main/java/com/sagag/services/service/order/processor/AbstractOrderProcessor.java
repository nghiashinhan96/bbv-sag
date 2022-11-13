package com.sagag.services.service.order.processor;

import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.order.EshopOrderHistoryState;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractOrderProcessor {

  @Autowired
  protected OrderExternalService orderExtService;

  @Autowired
  protected OrderHistoryRepository orderHistoryRepo;

  @Autowired
  protected CouponCacheService couponCacheService;

  /**
   * Defaults AX Order type for request to AX.
   *
   */
  public String axOrderType(UserInfo user, OrderConditionContextBody orderCondition) {
    return StringUtils.EMPTY;
  }

  /**
   * Returns the order history type.
   *
   */
  public abstract OrderType orderHistoryType();

  /**
   * Checks the flag to allow send order confirmation email to user.
   *
   */
  public abstract boolean allowSendOrderConfirmationMail(UserInfo user);

  /**
   * Provides methodology to send order to external system.
   *
   * @param companyName
   * @param request
   * @return
   */
  public abstract OrderConfirmation executeSendOrder(String companyName,
      ExternalOrderRequest request) throws ServiceException;

  /***/
  public abstract OrderRequestType orderRequestType();

  /**
   * Returns the order detail url.
   *
   * @param orderConfirm
   * @param custNr
   * @return
   */
  public String prepareOrderDetailUrl(final OrderConfirmation orderConfirm, String custNr) {
    log.debug("Preparing order detail url with orderConfirm = {} - custNr {}",
        orderConfirm, custNr);
    return StringUtils.defaultIfBlank(orderConfirm.getAxOrderURL(), StringUtils.EMPTY);
  }


  public long updateOrderHistory(final OrderHistory orderHistory,
      final OrderConfirmation orderConfirm, EshopOrderHistoryState eshopOrderHistoryState,
      final String customerNr) {

    Asserts.notNull(orderHistory, "Order history should not be null!");
    orderHistory.setOrderState(eshopOrderHistoryState.name());
    orderHistory.setCreatedDate(Calendar.getInstance().getTime());

    if (!Objects.isNull(orderConfirm)) {
      final String axOrderDetailUrl = prepareOrderDetailUrl(orderConfirm, customerNr);
      final String workIds =
          !CollectionUtils.isEmpty(orderConfirm.getWorkIds()) ? orderConfirm.getWorkIds().stream()
              .collect(Collectors.joining(SagConstants.COMMA_NO_SPACE)) : StringUtils.EMPTY;

      orderHistory.setErpOrderDetailUrl(axOrderDetailUrl);
      orderHistory.setOrderNumber(orderConfirm.getOrderNr());
      orderHistory.setWorkIds(workIds);
    }

    return orderHistoryRepo.save(orderHistory).getId();
  }

  public abstract ExecutionOrderType orderExecutionType();

}
