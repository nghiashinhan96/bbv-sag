package com.sagag.services.service.order;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.exception.OrderException.OrderErrorCase;
import com.sagag.services.service.mail.orderconfirmation.OrderConfirmationCriteria;
import com.sagag.services.service.mail.orderconfirmation.OrderConfirmationMailSender;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.order.model.OrderValidateCriteria;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;
import com.sagag.services.service.order.processor.OrderProcessorFactory;
import com.sagag.services.service.order.steps.AfterOrderHandler;
import com.sagag.services.service.order.steps.InitializeOrderRequestStepHandler;
import com.sagag.services.service.order.steps.InitializingOrderHistoryStepHandler;
import com.sagag.services.service.order.steps.OrderingStepHandler;
import com.sagag.services.service.order.validator.OrderValidator;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderConditionContextBody;
import com.sagag.services.service.request.order.OrderContextBuilder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderHandlerContextV2 {

  private static final String LOG_INFO = "Eshop start creating order for user = {}, "
      + "orderRequest = {} and order request type = {}";

  @Autowired
  private InitializeOrderRequestStepHandler initializeOrderRequestStepHandler;

  @Autowired
  private InitializingOrderHistoryStepHandler initializingOrderHistoryStepHandler;

  @Autowired
  private OrderingStepHandler orderingStepHandler;

  @Autowired
  private OrderConfirmationMailSender orderConfirmationMailSender;

  @Autowired
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Autowired
  private CustomerExternalService customerExtService;

  @Autowired
  private OrderValidator orderValidator;

  @Autowired
  private AfterOrderHandler afterOrderHandler;

  @Autowired
  private FinalCustomerOrderService finalCustomerOrderService;

  @Autowired
  private OrderProcessorFactory orderProcessorFactory;

  @LogExecutionTime
  @Transactional
  public OrderConfirmation handleSingleOrderProcess(final UserInfo user,
      final OrderContextBuilder orderBuilder, ShoppingCart shoppingCart)
      throws ValidationException {
    CreateOrderRequestBodyV2 body = orderBuilder.getBody();
    OrderRequestType orderRequestType = orderBuilder.getOrderRequestType();
    log.info(LOG_INFO, user, body, orderRequestType, shoppingCart);
    Assert.notNull(body, "The given order request body must not be null");
    Assert.notNull(orderBuilder.getSelectedOrderCondition(),
        "The given order condition body must not be null");

    // #4240: [STG-CZ]: AX9 Orders- Payment Type handling
    // Force map payment method if no choose on GUI, should improve after have ticket
    if (StringUtils.isBlank(orderBuilder.getSelectedOrderCondition().getPaymentMethod())) {
      orderBuilder.getSelectedOrderCondition()
          .setPaymentMethod(user.getCustomer().getCashOrCreditTypeCode());
    }

    orderValidator.validate(new OrderValidateCriteria(body,
        orderBuilder.getSelectedOrderCondition(), user, orderRequestType));

    final String affShortName = user.getAffiliateShortName();
    final SupportedAffiliate affiliate = supportedAffiliateRepo.findFirstByShortName(affShortName)
        .orElseThrow(() -> new IllegalArgumentException("Not found supported affiliate"));

    final AbstractOrderProcessor processor =
        orderProcessorFactory.getOrderingProcessor(orderRequestType);
    shoppingCart.getItems()
        .forEach(item -> item.setReference(body.getAdditionalTextDocs().get(item.getCartKey())));

    final OrderConditionContextBody orderCondition = orderBuilder.getSelectedOrderCondition();
    final String axOrderType = processor.axOrderType(user, body.getOrderCondition());
    final ExecutionOrderType orderExecutionType = processor.orderExecutionType();
    final ExternalOrderRequest orderRequest = initializeOrderRequestStepHandler.handle(user,
            affiliate, axOrderType, shoppingCart.getItems(), orderBuilder, orderExecutionType);

    // Initial value for totalInclVat
    double totalInclVat = 0;
    if (user.getSettings().isCurrentStateNetPriceView()) {
      totalInclVat = Double
          .valueOf(SagPriceUtils.roundHalfEvenTo2digits(shoppingCart.getNewTotalWithNetAndVat()));
    } else {
      totalInclVat =
          Double.valueOf(SagPriceUtils.roundHalfEvenTo2digits(shoppingCart.getNewTotalWithVat()));
    }

    // Initial order history
    final OrderHistory orderHistory = initializingOrderHistoryStepHandler.handle(user, processor,
        shoppingCart, body.getAdditionalTextDocs(), orderCondition.getInvoiceTypeCode(),
        orderRequest, totalInclVat);

    // Handle ordering step
    final OrderConfirmation orderConfirmRes =
        orderingStepHandler.handle(user, processor, orderRequest, orderHistory);
    if (!Objects.isNull(orderBuilder.getCoupon().getWarningMessageCode())) {
      orderConfirmRes.setWarningMsgCode(orderBuilder.getCoupon().getWarningMessageCode().name());
    }
    attachMoreInfoToOrderConfirmResponse(shoppingCart, orderBuilder, orderExecutionType,
        orderConfirmRes);

    if (!Objects.isNull(body.getFinalCustomerOrderId())) {
      finalCustomerOrderService.changeOrderStatusToOrdered(body.getFinalCustomerOrderId());
    }

    if (isOrderDoneWithoutError(orderConfirmRes)) {
      // After ordering successfully, handle some steps after that
      AfterOrderCriteria afterOrderCriteria = AfterOrderCriteria.builder()
          .shoppingCart(shoppingCart).axResult(orderConfirmRes).orderHistory(orderHistory)
          .couponUseLog(orderBuilder.getCoupon().getCouponUseLog())
          .shopType(orderBuilder.getShopType())
          .goodsReceiverId(
              Objects.isNull(body.getFinalCustomer()) ? null : body.getFinalCustomer().getOrgId())
          .finalCustomerOrderId(body.getFinalCustomerOrderId())
          .reference(orderRequest.getCustomerRefText()).branchRemark(orderRequest.getMessage())
          .externalOrderRequest(orderRequest)
          .totalPrice(totalInclVat)
          .userInfo(user)
          .additionalTextDocs(body.getAdditionalTextDocs())
          .build();

      afterOrderHandler.handle(user, processor, afterOrderCriteria);

      // Verify the send mail setting
      if (!processor.allowSendOrderConfirmationMail(user)) {
        return orderConfirmRes;
      }

      // #2418: Send mail just apply for customer view and sales on behalf create order is
      // successfully.
      // If user is sales on behalf and transfer basket we don't send any email for end-user
      // Build mail content
      final String company = affiliate.getCompanyName();
      final Address deliveryAddr = customerExtService.findCustomerAddressById(company,
          user.getCustNrStr(), orderRequest.getDeliveryAddressId()).orElse(null);
      final Address billingAddr = customerExtService
          .findCustomerAddressById(company, user.getCustNrStr(), orderRequest.getInvoiceAddressId())
          .orElse(null);
      orderConfirmationMailSender.send(OrderConfirmationCriteria.of(user, orderBuilder,
          orderRequest, shoppingCart, orderConfirmRes, deliveryAddr, billingAddr));
    }
    return orderConfirmRes;
  }

  private boolean isOrderDoneWithoutError(final OrderConfirmation orderConfirmRes) {
    return Objects.isNull(orderConfirmRes.getErrorMsg()) || isTimeoutOrder(orderConfirmRes);
  }

  private boolean isTimeoutOrder(final OrderConfirmation orderConfirmRes) {
    return StringUtils.equalsIgnoreCase(orderConfirmRes.getErrorMsg().getKey(),
        OrderErrorCase.OE_STO_001.key());
  }

  private void attachMoreInfoToOrderConfirmResponse(ShoppingCart shoppingCart,
      final OrderContextBuilder orderBuilder, ExecutionOrderType orderExecutionType,
      final OrderConfirmation orderConfirmRes) {
    orderConfirmRes.setOrderType(orderBuilder.getOrderRequestType().getJsonEventType());
    orderConfirmRes.setCartKeys(shoppingCart.getItems().stream().map(ShoppingCartItem::getCartKey)
        .collect(Collectors.toList()));
    orderConfirmRes.setSubTotalWithNet(shoppingCart.getSubTotalWithNet());
    orderConfirmRes.setVatTotalWithNet(shoppingCart.getVatTotalWithNet());
    orderConfirmRes.setOrderExecutionType(orderExecutionType.name());
    orderConfirmRes.setLocation(orderBuilder.getLocation());

    Optional.ofNullable(orderConfirmRes.getOrderAvailabilities())
        .filter(CollectionUtils::isNotEmpty)
        .ifPresent(avails -> avails.forEach(avail ->
          shoppingCart.getItems().forEach(item -> {
            if (item.getArticle().getArtid()
                .equalsIgnoreCase(avail.getAvailabilityResponse().getArticleId())) {
              avail.setArticle(item.getArticle());
              avail.setAttachArticles(item.getAttachedArticles());
              avail.setPfand(item.isPfand());
              avail.setRecycle(item.isRecycle());
              avail.setVoc(item.isVoc());
              avail.setVrg(item.isVrg());
              avail.setItemType(item.getItemType().name());
            }
          })));
  }

}
