package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.order.FinalCustomerOrderItemRepository;
import com.sagag.eshop.repo.api.order.FinalCustomerOrderRepository;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.OrderHistoryFilterDto;
import com.sagag.eshop.service.dto.order.VCustomerOrderHistoryDto;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.article.api.request.OrderStatusRequest;
import com.sagag.services.article.api.request.SalesOrderStatus;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPosition;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.order.OrderDetailDto;
import com.sagag.services.hazelcast.domain.order.OrderDetailDto.OrderDetailDtoBuilder;
import com.sagag.services.hazelcast.domain.order.OrderHistoryFilters;
import com.sagag.services.hazelcast.domain.order.OrderInfoDto;
import com.sagag.services.hazelcast.domain.order.OrderItemDetailDto;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.ivds.executor.IvdsArticleAmountTaskExecutor;
import com.sagag.services.service.api.OrderBusinessService;
import com.sagag.services.service.cart.operation.add.AddCartItemsFromArtNumbersShoppingCartOperation;
import com.sagag.services.service.exception.OrderStatusException;
import com.sagag.services.service.exception.OrderStatusException.OrderStatusCase;
import com.sagag.services.service.order.handler.OrderHandler;
import com.sagag.services.service.order.history.OrderHistoryHandler;
import com.sagag.services.service.order.history.OrderHistoryHelper;
import com.sagag.services.service.order.history.OrderHistoryInformationBeautifier;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.order.ordercondition.OrderConditionInitializer;
import com.sagag.services.service.order.steps.ShoppingCartConverters;
import com.sagag.services.service.request.ViewOrderHistoryRequestBody;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderHistoryDetailRequestBody;
import com.sagag.services.service.request.order.OrderStatusRequestBody;
import com.sagag.services.service.utils.order.OrdersUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Order business service implementation class.
 */
@Service
@Transactional
@Slf4j
public class OrderBusinessServiceImpl implements OrderBusinessService {

  @Autowired
  private OrderExternalService orderExternalService;

  @Autowired
  private OrderHistoryService orderHistoryService;

  @Autowired
  private IvdsArticleAmountTaskExecutor ivdsArticleAmountTaskExecutor;

  @Autowired
  private OrderHistoryHelper orderHistoryHelper;

  @Autowired
  private AddCartItemsFromArtNumbersShoppingCartOperation addCartItemFromArtNrsOperation;

  @Autowired
  private FinalCustomerOrderItemRepository finalCustomerOrderItemRepo;

  @Autowired
  private FinalCustomerOrderRepository finalCustomerOrderRepo;

  @Autowired
  private OrderHandler orderHandler;

  @Autowired
  private OrderConditionInitializer orderConditionInitializer;

  @Autowired
  private OrderHistoryHandler orderHistoryHandler;

  @Autowired
  private OrderHistoryInformationBeautifier orderHistoryInfoBeautifier;

  @Override
  public List<OrderConfirmation> createOrder(UserInfo user, CreateOrderRequestBodyV2 body,
      ApiRequestType type, ShopType shopType, Boolean ksoForcedDisabled) throws ServiceException {
    return orderHandler.handleOrder(user, body, type, shopType, ksoForcedDisabled);
  }

  @Override
  public OrderHistoryFilters getFilters(final UserInfo user) {
    return orderHistoryHandler.getFilters(user);
  }

  @Override
  public List<VCustomerOrderHistoryDto> searchOrders(UserInfo user,
      ViewOrderHistoryRequestBody condition) throws ResultNotFoundException {
    long start = System.currentTimeMillis();
    if (user.isSalesAssistantRole()) {
      throw new IllegalArgumentException("It does not support sale user");
    }
    String customerName = user.getCompanyName();
    String customerNr = user.getCustNrStr();
    Map<String, ExternalOrderDetail> externalOrderMap =
        getExternalOrdersByCustomer(customerName, customerNr, condition);
    log.debug("OrderBusinessServiceImpl->getOrders-> Get Ax order list by customer {} ms",
        System.currentTimeMillis() - start);

    final Map<String, VCustomerOrderHistory> orderMap; // key:orderNumber, value:orderHistory
    Date beginOfDateFrom = OrdersUtils.getDateFrom(condition.getFrom());
    Date endOfDateTo = OrdersUtils.getDateTo(condition.getTo());

    if (user.isUserAdminRole() || user.isOciVirtualUser()) {
      orderMap = orderHistoryService.getOrdersByCustomerAndDate(user.getCustNrStr(),
          beginOfDateFrom, endOfDateTo);
    } else {
      orderMap =
          orderHistoryService.getOrdersByUserAndDate(user.getId(), beginOfDateFrom, endOfDateTo);
    }

    List<VCustomerOrderHistoryDto> orderDetailDtos = mergeExternalAndEshopOrders(
        orderHistoryHandler.doFilterOrderHistory(orderMap, externalOrderMap, condition),
        externalOrderMap, condition, customerName, customerNr);

    orderHistoryHelper.updateGoodsReceiverInfo(user, orderDetailDtos,
        orderMap.values().stream().collect(Collectors.toList()));

    sortOrderHistoryByDate(orderDetailDtos);

    return orderDetailDtos;
  }

  private Map<String, ExternalOrderDetail> getExternalOrdersByCustomer(String companyName,
      String customerNr, ViewOrderHistoryRequestBody searchCondition) {
    final List<ExternalOrderDetail> externalOrders = new ArrayList<>();
    Integer firstPage = 1;
    updateFullOrders(externalOrders, companyName, String.valueOf(customerNr), searchCondition,
        firstPage);

    return externalOrders.stream()
        .collect(Collectors.toMap(ExternalOrderDetail::getNr, order -> order));
  }

  private List<VCustomerOrderHistoryDto> mergeExternalAndEshopOrders(
      Map<String, VCustomerOrderHistory> orderMap,
      Map<String, ExternalOrderDetail> externalOrderMap, ViewOrderHistoryRequestBody condition,
      String customerName, String customerNumber) {
    Set<String> orderNrs = new HashSet<>();
    orderNrs.addAll(externalOrderMap.keySet());
    orderNrs.addAll(orderMap.keySet());

    final List<VCustomerOrderHistoryDto> orderList = new ArrayList<>();
    orderNrs.forEach(orderNr -> {
      final VCustomerOrderHistory orderHistory = orderMap.get(orderNr);
      final String username =
          Objects.isNull(orderHistory) ? StringUtils.EMPTY : orderHistory.getUsername();
      Optional<VCustomerOrderHistoryDto> orderDetailDto =
          VCustomerOrderHistoryDto.createFitleringOrderDetailInfo(externalOrderMap.get(orderNr),
              orderHistory, SagBeanUtils.map(condition, OrderHistoryFilterDto.class), customerName,
              customerNumber, username);
      if (orderDetailDto.isPresent()) {
        orderList.add(orderDetailDto.get());
      }
    });
    return orderList;
  }

  private void updateFullOrders(List<ExternalOrderDetail> erpOrders, String companyName,
      String customerNr, ViewOrderHistoryRequestBody body, Integer page) {
    Optional<ExternalOrderHistory> orderHistory =
        orderHistoryHandler.handleExternalOrderHistory(companyName, customerNr, body, page);
    if (!orderHistory.isPresent() || Objects.isNull(orderHistory.get().getOrders())) {
      log.debug("Order list is empty.");
      return;
    }
    erpOrders.addAll(orderHistory.get().getOrders());
    if (orderHistory.get().hasNext()) {
      // From PO #1460: sends multiple requests to get the full order
      // history list
      Integer nextPage = page + 1;
      updateFullOrders(erpOrders, companyName, customerNr, body, nextPage);
    }
  }

  @Override
  public OrderDetailDto getOrderDetail(final UserInfo user,
      final OrderHistoryDetailRequestBody body) throws ResultNotFoundException {
    final String curAffiliate = body.getAffiliate();
    final Long orderId = body.getOrderId();
    final String orderNumber = body.getOrderNumber();
    String companyName = SupportedAffiliate.fromDesc(curAffiliate).getCompanyName();
    OrderDetailDtoBuilder orderDetailBuilder = OrderDetailDto.builder();
    final boolean hasEshopOrder = !Objects.isNull(orderId) && orderId > 0;
    if (hasEshopOrder) {
      orderDetailBuilder.username(user.getUsername());
      buildEshopOrderDetail(user, orderId, orderDetailBuilder);
    } else {
      buildExternalOrderDetail(user, orderNumber, orderDetailBuilder);
    }
    if (user.isSalesNotOnBehalf()) {
      return orderDetailBuilder.build();
    }
    final String custNr = body.getCustomerNr();
    Assert.hasText(custNr, "The given customer number must not be null or empty");
    final Optional<ExternalOrderHistory> externalOrderHistory =
        orderHistoryHandler.handleExternalOrderHistoryDetail(orderNumber, companyName, custNr);

    if (!externalOrderHistory.isPresent()
        || CollectionUtils.isEmpty(externalOrderHistory.get().getOrders())) {
      return orderDetailBuilder.build();
    }

    Optional<ExternalOrderDetail> axOrderDetail =
        externalOrderHistory.get().getOrders().stream().findFirst();

    if (axOrderDetail.isPresent()) {
      orderDetailBuilder.nr(axOrderDetail.get().getNr()).typeCode(axOrderDetail.get().getTypeCode())
          .type(axOrderDetail.get().getType()).typeDesc(axOrderDetail.get().getTypeDesc())
          .sendMethodCode(axOrderDetail.get().getSendMethod())
          .sendMethodDesc(axOrderDetail.get().getSendMethodDesc())
          .sendMethod(axOrderDetail.get().getSendMethod())
          .statusCode(axOrderDetail.get().getStatusCode()).status(axOrderDetail.get().getStatus())
          .statusDesc(axOrderDetail.get().getStatusDesc());
      if (!hasEshopOrder) {
        orderDetailBuilder.date(axOrderDetail.get().getDate());
      }
    }

    return orderDetailBuilder.build();
  }

  private void buildExternalOrderDetail(final UserInfo user, String orderNr,
      OrderDetailDtoBuilder orderDetailBuilder) throws ResultNotFoundException {
    if (user.isSalesNotOnBehalf()) {
      throw new IllegalArgumentException(
          "Sale not on behalf does not have permission to see this AX order detail");
    }
    String companyName = user.getCompanyName();
    String customerNr = user.getCustNrStr();
    long start = System.currentTimeMillis();
    final Optional<ExternalOrderPositions> orderPosition =
        orderHistoryHandler.handlelExternalOrderPosistionHistory(orderNr, companyName, customerNr);
    log.debug(
        "OrderBusinessServiceImpl->buildExternalOrderDetail-> Get order position from external {} ms",
        System.currentTimeMillis() - start);
    if (!orderPosition.isPresent() || Objects.isNull(orderPosition.get().getPositions())) {
      throw new ResultNotFoundException(String.format("Order %s is not found", orderNr));
    }

    Map<String, Integer> articleIdQuantityMap = orderPosition.get().getPositions().stream()
        .collect(Collectors.toMap(ExternalOrderPosition::getArticleId,
            ExternalOrderPosition::getQuantity, (articleId1, articleId2) -> articleId1));

    List<OrderItemDetailDto> items = new ArrayList<>();

    start = System.currentTimeMillis();
    articleIdQuantityMap.forEach((articleId, quantity) -> {
      final Page<ArticleDocDto> articles =
          ivdsArticleAmountTaskExecutor.execute(user, articleId, quantity, Optional.empty());
      if (articles.hasContent()) {
        ArticleDocDto article = articles.getContent().stream().findFirst().get();
        OrderItemDetailDto item = OrderItemDetailDto.builder().article(article).build();
        items.add(item);
      }
    });
    log.debug("OrderBusinessServiceImpl->buildExternalOrderDetail-> search article by amount {} ms",
        System.currentTimeMillis() - start);

    orderDetailBuilder.orderItems(items);
    orderDetailBuilder.invoiceTypeCode(StringUtils.EMPTY);
    orderDetailBuilder.reference(StringUtils.EMPTY);
    orderDetailBuilder.branchRemark(StringUtils.EMPTY);
    orderDetailBuilder.vehicleInfos(Collections.emptyList());
  }

  private void buildEshopOrderDetail(UserInfo user, Long orderId,
      OrderDetailDtoBuilder orderDetailBuilder) throws ResultNotFoundException {
    long start = System.currentTimeMillis();
    OrderHistory orderHistory =
        orderHistoryService.getOrderDetail(orderId).orElseThrow(() -> new ResultNotFoundException(
            String.format("Order with order id = %s is not found", orderId)));
    log.debug("OrderBusinessServiceImpl->buildEshopOrderDetail-> Get order history from db {} ms",
        System.currentTimeMillis() - start);

    orderDetailBuilder.id(orderHistory.getId());
    orderDetailBuilder.customerNr(orderHistory.getCustomerNumber());

    if (!StringUtils.isBlank(orderHistory.getOrderInfoJson())) {
      final OrderInfoDto orderInfo =
          OrderInfoDto.createOrderInfoDtoFromJson(orderHistory.getOrderInfoJson());
      orderHistoryInfoBeautifier.beautify(user, orderInfo, false);
      orderDetailBuilder.nr(orderHistory.getOrderNumber());
      orderDetailBuilder.date(DateUtils.getUTCDateStrWithTimeZone(orderHistory.getCreatedDate()));
      orderDetailBuilder.orderItems(orderInfo.getItems());
      orderDetailBuilder.invoiceTypeCode(orderInfo.getInvoiceTypeCode());
      orderDetailBuilder.reference(orderInfo.getReference());
      orderDetailBuilder.branchRemark(orderInfo.getBranchRemark());
      orderDetailBuilder.vehicleInfos(orderInfo.getVehicleInfosFromOrderItem());
      orderDetailBuilder.showPriceType(BooleanUtils.isTrue(orderInfo.getShowPriceType()));
    }
    orderHistoryHelper.updateFinalCustomerInfo(user, orderDetailBuilder, orderHistory);
  }

  private void sortOrderHistoryByDate(List<VCustomerOrderHistoryDto> orders) {
    orders.sort((VCustomerOrderHistoryDto left, VCustomerOrderHistoryDto right) -> {
      if (!Objects.isNull(right) && !Objects.isNull(left)) {
        return right.getDate().compareTo(left.getDate());
      }
      if (!Objects.isNull(right)) {
        return 1;
      }
      if (!Objects.isNull(left)) {
        return -1;
      }
      return 0;
    });
  }

  @Override
  @Transactional
  public void updateSalesOrderStatus(String affiliateName, OrderStatusRequestBody request)
      throws OrderStatusException {
    log.debug("Updating sales order status with affiliate = {} - request = {}", affiliateName,
        request);
    Assert.hasText(affiliateName, "The given affiliate must not be empty");
    Assert.notNull(request, "The given request must not be null");

    boolean changed;
    try {
      final OrderStatusRequest axRequest =
          OrderStatusRequest.of(request.getOrderNr(), SalesOrderStatus.FREI);
      changed = orderExternalService.updateSalesOrderStatus(affiliateName, axRequest);
    } catch (RestClientException e) {
      throw new OrderStatusException(OrderStatusCase.COS_STO_001, e);
    }

    if (!changed) {
      throw new OrderStatusException(OrderStatusCase.COS_STO_002, "Changed order status is false");
    }

    final long orderHistoryId = request.getOrderHistoryId();
    orderHistoryService.updateClosedDate(orderHistoryId, Calendar.getInstance().getTime());
  }

  @Override
  public EshopBasketContext initializeOrderConditions(final UserInfo user) {
    return orderConditionInitializer.initialize(user);
  }

  @Override
  @Transactional
  public void updateOrder(UserInfo user, CreateOrderRequestBodyV2 body) throws ServiceException {
    if (Objects.isNull(body.getFinalCustomerOrderId())) {
      return;
    }
    List<FinalCustomerOrderItem> originFinalCustomerOrder =
        this.finalCustomerOrderItemRepo.findByFinalCustomerOrderId(body.getFinalCustomerOrderId());
    this.finalCustomerOrderItemRepo.deleteAll(originFinalCustomerOrder);
    ShoppingCart finalCustomerCart =
        addCartItemFromArtNrsOperation.checkoutCart(user, ShopType.SUB_FINAL_SHOPPING_CART);
    finalCustomerCart.getItems()
        .forEach(item -> item.setReference(body.getAdditionalTextDocs().get(item.getCartKey())));

    List<FinalCustomerOrderItem> finalCustomerOrderItems =
        ShoppingCartConverters.converter(finalCustomerCart, user.isFinalUserRole());
    finalCustomerOrderItems.stream()
        .forEach(item -> item.setFinalCustomerOrderId(body.getFinalCustomerOrderId()));
    this.finalCustomerOrderItemRepo.saveAll(finalCustomerOrderItems);
    Optional<FinalCustomerOrder> finalCustomerOrder =
        finalCustomerOrderRepo.findById(body.getFinalCustomerOrderId());
    final double grossTotalExclVat = finalCustomerCart.getSubTotal();
    final double finalCustomerNetTotalExclVat = finalCustomerCart.getFinalCustomerNetTotalExclVat();
    final double totalInclVat = getTotalInclVat(finalCustomerCart);
    final Double totalNetInclVat = getTotalFinalCustomerNetInclVat(user, finalCustomerCart);
    finalCustomerOrder.filter(val -> val.getTotalGrossPrice() != totalInclVat)
        .ifPresent(finalCustOrder -> {
          finalCustOrder.setTotalGrossPriceWithVat(totalInclVat);
          finalCustOrder.setTotalFinalCustomerNetPriceWithVat(totalNetInclVat);
          finalCustOrder.setTotalGrossPrice(grossTotalExclVat);
          finalCustOrder.setTotalFinalCustomerNetPrice(finalCustomerNetTotalExclVat);
          finalCustomerOrderRepo.save(finalCustOrder);
        });
  }

  private Double getTotalFinalCustomerNetInclVat(UserInfo user, ShoppingCart finalCustomerCart) {
    if (user.hasWholesalerPermission()) {
      return null;
    }
    return Double.valueOf(SagPriceUtils
        .roundHalfEvenTo2digits(finalCustomerCart.getSubTotalWithFinalCustomerNetAndVat()));
  }

  private double getTotalInclVat(ShoppingCart finalCustomerCart) {
    return Double.valueOf(SagPriceUtils
        .roundHalfEvenTo2digits(finalCustomerCart.getNewTotalWithVat()));
  }
}
