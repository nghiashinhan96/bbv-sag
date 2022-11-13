package com.sagag.services.service.api;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.VCustomerOrderHistoryDto;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.order.OrderDetailDto;
import com.sagag.services.hazelcast.domain.order.OrderHistoryFilters;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.exception.OrderStatusException;
import com.sagag.services.service.order.model.ApiRequestType;
import com.sagag.services.service.request.ViewOrderHistoryRequestBody;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderHistoryDetailRequestBody;
import com.sagag.services.service.request.order.OrderStatusRequestBody;

import java.util.List;

/**
 * Interface to define Order business service APIs.
 */
/**
 * @author quiluc
 *
 */
public interface OrderBusinessService {

  /**
   * Creates order by order request type.
   *
   * @param user user the user who request to create order
   * @param request request the request data
   * @param type the order request type
   * @param ksoForcedDisabled
   * @return the order confirmation after created successfully
   * @throws ServiceException
   */
  List<OrderConfirmation> createOrder(UserInfo user, CreateOrderRequestBodyV2 body, ApiRequestType type,
      ShopType shopType, Boolean ksoForcedDisabled) throws ServiceException;

  /**
   * Returns a historical order list for specific user.
   *
   * @param user the user who requests
   * @param body the body of request included condition search
   * @return the list of order detail
   * @throws ResultNotFoundException
   */
  List<VCustomerOrderHistoryDto> searchOrders(UserInfo user, ViewOrderHistoryRequestBody body)
      throws ResultNotFoundException;

  /**
   * Gets order history detail by order id and externalOrderUrl.
   *
   * @param curAffiliate the working affiliate
   * @param user the logging user info
   * @param body the order request body
   * @return the {@link OrderDetailDto}
   */
  OrderDetailDto getOrderDetail(UserInfo user, OrderHistoryDetailRequestBody body)
      throws ResultNotFoundException;

  /**
   * Filters the order histories.
   *
   * @param user the user who requests
   * @return the order history filters
   */
  OrderHistoryFilters getFilters(final UserInfo user);

  /**
   * Updates sales order status by order number.
   *
   * @param affiliateName the affiliate company name
   * @param request the changed order status request
   * @return the <code>true</code>, otherwise
   */
  void updateSalesOrderStatus(String affiliateName, OrderStatusRequestBody request)
      throws OrderStatusException;

  /**
   * Initializes order condition for user
   *
   * @param user the user who request to update
   * @return the object of {@link EshopBasketContext}
   */
  EshopBasketContext initializeOrderConditions(final UserInfo user);

  /**
   * Update order for final customer.
   *
   * @param user the user info
   * @param body the order request body
   * @throws ServiceException
   */
  void updateOrder(UserInfo user, CreateOrderRequestBodyV2 body) throws ServiceException;

}
