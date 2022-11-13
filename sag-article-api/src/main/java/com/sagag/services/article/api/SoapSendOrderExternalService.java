package com.sagag.services.article.api;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.domain.sag.erp.OrderConfirmation;

import java.util.Optional;

public interface SoapSendOrderExternalService {

  /**
   * Executes send order request.
   *
   * @param username
   * @param customerId
   * @param securityToken
   * @param language
   * @param request
   * @param additional
   * @return the result object of <code>OrderConfirmation</code>
   */
  OrderConfirmation sendOrder(String username, String customerId,
      String securityToken, String language, ExternalOrderRequest request,
      AdditionalSearchCriteria additional) throws ServiceException;

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
   * Get order position from erp
   *
   * @param orderNr
   * @param companyName
   * @param customerNr
   * @return
   */
  Optional<ExternalOrderPositions> getExternalOrderPosistion(String orderNr, String companyName,
      String customerNr);
}
