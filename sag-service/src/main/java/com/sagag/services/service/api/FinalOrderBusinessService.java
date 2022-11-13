package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.OrderDashboardDto;
import com.sagag.eshop.service.dto.order.VFinalCustomerOrderDto;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.service.request.FinalCustomerOrderSearchRequest;

import org.springframework.data.domain.Page;

/**
 * Interface to define wholesaler Order business service APIs.
 */
public interface FinalOrderBusinessService {

  /**
   * Gets order dashboard overview for wholesaler user.
   *
   * @param user the user who request to update
   * @return the object of {@link OrderDashboardDto}
   */
  OrderDashboardDto getOrderDashBoardOverview(UserInfo user);

  /**
   * Searches final customer order.
   *
   * @param body the FinalCustomerOrderSearchRequest body
   * @param user the user who request to update
   * @return Page of {@link VFinalCustomerOrderDto}
   */
  Page<VFinalCustomerOrderDto> searchFinalCustomerOrder(FinalCustomerOrderSearchRequest body, UserInfo user);

  /**
   * Export final customer order in csv format.
   *
   * @param finalCustomerOrderId the final customer order id
   * @param user the user who request to update
   * @return Stream of {@link ExportStreamedResult}
   */
  ExportStreamedResult exportFinalCustomerOrderCsv(UserInfo user, Long finalCustomerOrderId)
      throws ServiceException;

  /**
   * Export final customer order in excel format.
   *
   * @param finalCustomerOrderId the final customer order id
   * @param user the user who request to update
   * @return Stream of {@link ExportStreamedResult}
   */
  ExportStreamedResult exportFinalCustomerOrderExcel(UserInfo user, Long finalCustomerOrderId)
      throws ServiceException;
}
