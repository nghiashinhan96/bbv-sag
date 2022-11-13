package com.sagag.eshop.service.api;


import com.sagag.eshop.repo.criteria.OrderHistorySearchCriteria;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.service.dto.order.SaleOrderHistoryDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface to define Order history service APIs.
 */
public interface OrderHistoryService {

  /**
   * Returns the order detail of order history.
   *
   * @param orderId the request order id
   * @return the result of order detail
   */
  Optional<OrderHistory> getOrderDetail(Long orderId);

  /**
   * Returns VCustomerOrderHistory map that matched customer and date.
   *
   * @param customerNr the customer number
   * @param dateFrom the start date to compare
   * @param dateTo the end date to compare
   * @return the map of VCustomerOrderHistory
   */
  Map<String, VCustomerOrderHistory> getOrdersByCustomerAndDate(String customerNr, Date dateFrom,
      Date dateTo);

  /**
   * Returns VCustomerOrderHistory map that matched user and date.
   *
   * @param userId the user id
   * @param dateFrom the start date to compare
   * @param dateTo the end date to compare
   * @return the map of VCustomerOrderHistory
   */
  Map<String, VCustomerOrderHistory> getOrdersByUserAndDate(Long userId, Date dateFrom,
      Date dateTo);

  /**
   * Returns the paging latest orders of a sale for the specific affiliate.
   *
   * @param criteria the order history search criteria
   * @param pageable the current page to request
   * @return the page-able histories of recent orders
   */
  Page<SaleOrderHistoryDto> getSaleOrders(OrderHistorySearchCriteria criteria, Pageable pageable);

  /**
   * Updates closed date by order history id.
   *
   * @param orderHistoryId the order history id
   * @param closedDate the closed date
   */
  void updateClosedDate(long orderHistoryId, Date closedDate);

  /**
   * Gets the latest order state for specified user.
   *
   * @param userId the user id
   * @return the latest order state for specified user
   */
  String findLatestOrderStateByUserId(Long userId);

  /**
   * Returns the available order numbers by user id.
   *
   * @param userId the request userId
   * @param orderNrs the order numbers need verify
   * @return the list of available order numbers
   */
  List<String> searchAvailableOrderNrs(Long userId, List<String> orderNrs);

  /**
   * Returns the order history by order number.
   *
   * @param orderNr the order number
   * @return the optional of {@link OrderHistory}
   */
  Optional<OrderHistory> searchOrderByNr(String orderNr);
}
