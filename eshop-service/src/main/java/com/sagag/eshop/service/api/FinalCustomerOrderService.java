package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerOrderCriteria;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderItemDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderReferenceDto;
import com.sagag.eshop.service.dto.order.OrderDashboardDto;
import com.sagag.eshop.service.dto.order.VFinalCustomerOrderDto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define final customer Order service APIs.
 */
public interface FinalCustomerOrderService {

  /**
   * Gets order dash board overview by orgCode
   *
   * @param orgCode the customer number
   * @return number of new orders by orgCode.
   */
  OrderDashboardDto getOrderDashboardByOrgCode(String orgCode);

  /**
   * Searches final customer order by criteria.
   *
   * @param criteria the search criteria
   * @return Page of {@link VFinalCustomerOrderDto}
   */
  Page<VFinalCustomerOrderDto> searchVFinalCustomerOrderByCriteria(
      FinalCustomerOrderCriteria criteria);

  /**
   * Gets final customer order detail or specified order.
   *
   * @param finalCustomerOrderId the order id
   * @return Optional of {@link FinalCustomerOrderDto}
   */
  Optional<FinalCustomerOrderDto> getFinalCustomerOrderDetail(Long finalCustomerOrderId);

  /**
   * Gets final customer order reference text for specified order.
   *
   * @param finalCustomerOrderId the order id
   * @return Optional of {@link FinalCustomerOrderReferenceDto}
   */
  Optional<FinalCustomerOrderReferenceDto> getFinalCustomerOrderReference(Long finalCustomerOrderId);

  /**
   * Saves FinalCustomerOrder.
   *
   * @param finalCustomerOrder the FinalCustomerOrder to save
   */
  void save(FinalCustomerOrder finalCustomerOrder);

  /**
   * Gets FinalCustomerOrderItems.
   *
   * @param finalCustomerOrderId
   * @return list of {@link FinalCustomerOrderItemDto}
   */
  List<FinalCustomerOrderItemDto> getFinalCustomerOrderItems(Long finalCustomerOrderId);

  /**
   * Delete open an order of final customer
   *
   * @param openOrderId the id of open order
   */
  void deleteOrder(Long openOrderId);

  /**
   * Change status of order
   *
   * @param orderId
   */
  void changeOrderStatusToOpen(Long orderId);

  /**
   * Change status of order to ordered
   *
   * @param orderId
   */
  void changeOrderStatusToOrdered(Long orderId);

}
