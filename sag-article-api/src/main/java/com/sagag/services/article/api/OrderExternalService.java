package com.sagag.services.article.api;

import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.article.api.request.OrderStatusRequest;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.domain.sag.erp.OrderConfirmation;

import java.util.Optional;

public interface OrderExternalService {

  /**
   * Creates an order from the order request.
   *
   * @param companyName the company name
   * @param request the {@link ExternalOrderRequest}
   * @return the {@link OrderConfirmation} response.
   */
  OrderConfirmation createOrder(String companyName, ExternalOrderRequest request);

  /**
   * Creates an basket from the order request.
   *
   * @param companyName the company name
   * @param request the {@link ExternalOrderRequest}
   * @return the {@link OrderConfirmation} response.
   */
  OrderConfirmation createBasket(String companyName, ExternalOrderRequest request);

  /**
   * Creates an offer from the order request.
   *
   * @param companyName the company name
   * @param request the {@link ExternalOrderRequest}
   * @return the {@link OrderConfirmation} response.
   */
  OrderConfirmation createOffer(String companyName, ExternalOrderRequest request);

  /**
   * Returns the histories of orders.
   *
   * @param relativeUrl the history relative url
   * @return the <code>ExternalOrderHistory</code> response.
   */
  Optional<ExternalOrderHistory> getExternalOrderByHrefLink(String relativeUrl);

  /**
   * Returns the histories of orders.
   *
   * @param companyName the company name
   * @param customerNr the customer number
   * @param request contain the order history filter
   * @param page the page of history
   * @return the <code>ExternalOrderHistory</code> response.
   */
  Optional<ExternalOrderHistory> getExternalOrderHistoryOfCustomer(String companyName,
      String customerNr, ExternalOrderHistoryRequest request, Integer page);

  /**
   * Gets order positions (order detail) from external.
   *
   * @param companyName the company name to search
   * @param customerNr the customer numbe to search
   * @param orderNr the order number to search
   * @return optional of <code>ExternalOrderPositions</code> instance.
   */
  Optional<ExternalOrderPositions> getOrderPositions(String companyName, String customerNr,
      String orderNr);

  /**
   * Updates sales order status by order number.
   *
   * @param compName the company name
   * @param request the changed status request
   * @return the changed status
   */
  boolean updateSalesOrderStatus(String compName, OrderStatusRequest request);

  /**
   * Returns the order details by order number.
   *
   * @param compName the company name
   * @param custNr the customer number
   * @param orderNr the order number
   */
  Optional<ExternalOrderHistory> getOrderDetailByOrderNr(String compName, String custNr,
      String orderNr);
}
