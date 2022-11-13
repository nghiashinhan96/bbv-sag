package com.sagag.services.rest.controller.orders;

import com.sagag.eshop.repo.criteria.OrderHistorySearchCriteria;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderReferenceDto;
import com.sagag.eshop.service.dto.order.SaleOrderHistoryDto;
import com.sagag.eshop.service.dto.order.VCustomerOrderHistoryDto;
import com.sagag.eshop.service.dto.order.VFinalCustomerOrderDto;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.hazelcast.domain.order.OrderDetailDto;
import com.sagag.services.hazelcast.domain.order.OrderHistoryFilters;
import com.sagag.services.rest.authorization.annotation.HasWholesalerPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.FinalOrderBusinessService;
import com.sagag.services.service.api.OrderBusinessService;
import com.sagag.services.service.exception.OrderStatusException;
import com.sagag.services.service.request.FinalCustomerOrderSearchRequest;
import com.sagag.services.service.request.SaleOrderSearchRequestBody;
import com.sagag.services.service.request.ViewOrderHistoryRequestBody;
import com.sagag.services.service.request.order.OrderHistoryDetailRequestBody;
import com.sagag.services.service.request.order.OrderStatusRequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for Orders.
 */
@RestController
@RequestMapping("/order")
@Api(tags = "Orders Management APIs")
public class OrdersController {

  @Autowired
  private OrderBusinessService orderService;

  @Autowired
  private OrderHistoryService orderHistoryService;

  @Autowired
  private FinalCustomerOrderService finalCustomerOrderService;

  @Autowired
  private FinalOrderBusinessService finalOrderBusinessService;

  /**
   * Returns the order histories.
   *
   * @param authed the authenticated user request
   * @param body the request body
   * @return list of <code>VCustomerOrderHistoryDto</code> instance
   * @throws ResultNotFoundException
   */
  @ApiOperation(
      value = ApiDesc.Order.GET_ORDER_HISTORY_DESC,
      notes = ApiDesc.Order.GET_ORDER_HISTORY_NOTE
      )
  @PostMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<VCustomerOrderHistoryDto> searchOrders(final OAuth2Authentication authed,
      @RequestBody ViewOrderHistoryRequestBody body) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final List<VCustomerOrderHistoryDto> orders = orderService.searchOrders(
        user, body);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(orders);
  }

  /**
   * Returns recent order history used for sale and sale on behalf.
   *
   * @param authed the authenticated user request
   * @param body the request body
   * @return page-able list of <code>SaleOrderHistoryDto</code> instance
   * @throws ResultNotFoundException
   */
  @ApiOperation(
      value = ApiDesc.Order.GET_SALE_ORDERS_DESC,
      notes = ApiDesc.Order.GET_SALE_ORDERS_NOTE
      )
  @PostMapping(value = "/sale/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<SaleOrderHistoryDto> getSaleOrders(final OAuth2Authentication authed,
      @RequestBody SaleOrderSearchRequestBody body) throws ResultNotFoundException {

    final UserInfo user = (UserInfo) authed.getPrincipal();
    Assert.isTrue(user.isSalesUser() || user.isSaleOnBehalf(), "This api supports sales only");
    final Long saleId = user.isSalesAssistantRole() ? user.getId() : user.getSalesId();
    final PageRequest paging = PageRequest.of(body.getPage(), body.getSize());


    final OrderHistorySearchCriteria criteria =
        SagBeanUtils.map(body, OrderHistorySearchCriteria.class);
    criteria.setSaleId(saleId);

    Page<SaleOrderHistoryDto> orderHistories = orderHistoryService.getSaleOrders(criteria, paging);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(orderHistories);
  }

  /**
   * Get order history filters.
   *
   * @param authed the logged in user
   * @return instance of <code>OrderHistoryFilters</code>
   */
  @ApiOperation(
      value = ApiDesc.Order.GET_FILTER_CONDITION_DESC,
      notes = ApiDesc.Order.GET_FILTER_CONDITION_NOTE
      )
  @GetMapping(value = "/history/filters", produces = MediaType.APPLICATION_JSON_VALUE)
  public OrderHistoryFilters getFilters(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return orderService.getFilters(user);
  }

  /**
   * Returns the order details.
   *
   * @param authed the authenticated user request
   * @param body the request body
   * @return {@link OrderDetailDto} instance
   */
  @PostMapping(value = "/history/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public OrderDetailDto viewOrderDetail(final OAuth2Authentication authed,
      @RequestBody OrderHistoryDetailRequestBody body) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (user.hasCust()) {
      body.setCustomerNr(user.getCustNrStr());
      body.setAffiliate(user.getAffiliateShortName());
    }

    final OrderDetailDto orderDetail = orderService.getOrderDetail(user, body);
    if (orderDetail == null) {
      throw new ResultNotFoundException("Not found this order number = " + body.getOrderNumber());
    }
    return orderDetail;
  }

  /**
   * Updates order status by sales.
   *
   * @param authed the current authorization
   * @param request the changed order status request
   */
  @PutMapping(value = "/sales/status/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.ACCEPTED)
  public void updateSalesOrderStatus(final OAuth2Authentication authed,
      @RequestBody final OrderStatusRequestBody request) throws OrderStatusException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    orderService.updateSalesOrderStatus(user.getCompanyName(), request);
  }

  /**
   * Returns the final customer order details.
   *
   * @param authed the authenticated user request
   * @return {@link FinalCustomerOrderDto} instance
   */
  @GetMapping(value = "/final-customer/{finalCustomerOrderId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public FinalCustomerOrderDto viewFinalCustomerOrderDetail(final OAuth2Authentication authed,
      @PathVariable("finalCustomerOrderId") Long finalCustomerOrderId)
      throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        finalCustomerOrderService.getFinalCustomerOrderDetail(finalCustomerOrderId));
  }

  /**
   * Returns the final customer order reference text.
   *
   * @param authed the authenticated user request
   * @return {@link FinalCustomerOrderDto} instance
   */
  @GetMapping(value = "/final-customer-order/{finalCustomerOrderId}/reference-text",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public FinalCustomerOrderReferenceDto getFinalCustomerOrderReference(final OAuth2Authentication authed,
      @PathVariable("finalCustomerOrderId") Long finalCustomerOrderId)
          throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        finalCustomerOrderService.getFinalCustomerOrderReference(finalCustomerOrderId));
  }

  /**
   * Returns final customer orders for wholesaler and final user.
   *
   * @param authed the authenticated user request
   * @param body the request body
   * @return page-able list of {@link FinalCustomerOrderDto} instance
   */
  @ApiOperation(
      value = ApiDesc.Order.GET_FINAL_CUSTOMER_ORDER_DESC,
      notes = ApiDesc.Order.GET_FINAL_CUSTOMER_ORDER_NOTE
      )
  @PostMapping(value = "/final-customer-order/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<VFinalCustomerOrderDto> getFinalCustomerOrder(final OAuth2Authentication authed,
      @RequestBody FinalCustomerOrderSearchRequest body) {
    final UserInfo user = (UserInfo) authed.getPrincipal();

    return finalOrderBusinessService.searchFinalCustomerOrder(body, user);
  }

  @GetMapping(value = "/final-customer-order/{finalCustomerOrderId}/export-csv",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @HasWholesalerPreAuthorization
  public ResponseEntity<byte[]> exportFinalCustomerOrderCsv(final OAuth2Authentication authed,
      @PathVariable("finalCustomerOrderId") Long finalCustomerOrderId) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return finalOrderBusinessService.exportFinalCustomerOrderCsv(user, finalCustomerOrderId)
        .buildResponseEntity();
  }

  @GetMapping(value = "/final-customer-order/{finalCustomerOrderId}/export-excel",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @HasWholesalerPreAuthorization
  public ResponseEntity<byte[]> exportFinalCustomerOrderExcel(final OAuth2Authentication authed,
      @PathVariable("finalCustomerOrderId") Long finalCustomerOrderId) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return finalOrderBusinessService.exportFinalCustomerOrderExcel(user, finalCustomerOrderId)
        .buildResponseEntity();
  }

  @ApiOperation(value = ApiDesc.Order.DELETE_ORDER_FINAL_CUSTOMER,
      notes = ApiDesc.Order.DELETE_ORDER_FINAL_CUSTOMER_NOTE)
  @DeleteMapping(value = "/final-customer-order/{openOrderId}/delete",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public void deleteOrder(@PathVariable Long openOrderId) {
    finalCustomerOrderService.deleteOrder(openOrderId);
  }

}
