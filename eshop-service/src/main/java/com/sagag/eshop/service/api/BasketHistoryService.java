package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.BasketHistoryCriteria;
import com.sagag.eshop.service.dto.BasketHistoryDto;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BasketHistoryService {

  /**
   * The service to create basket history by user login.
   *
   * @param basketHistory the new basket of {@link BasketHistoryDto}
   */
  void createBasketHistory(BasketHistoryDto basketHistory);

  /**
   * The service to search basket histories by search criteria.
   *
   * @param criteria the search criteria of {@link BasketHistoryCriteria}
   * @param salesMode the flag of sales mode
   * @return the page of {@link BasketHistoryDto}
   */
  Page<BasketHistoryDto> searchBasketHistoriesByCriteria(BasketHistoryCriteria criteria, boolean salesMode);

  /**
   * The service to count the total of basket histories by customer.
   *
   * @param customerNr
   * @return the total basket histories
   */
  long countBasketHistoriesByCustomer(String customerNr);

  /**
   * The service to count the total of all basket histories.
   *
   * @return the total basket histories
   */
  long countSalesBasketHistories(Long salesUserId);

  /**
   * Returns the detail of basket history by user id.
   *
   * @param basketId the id of basket history
   * @return the detail of basket history
   */
  Optional<BasketHistoryDto> getBasketHistoryDetails(final Long basketId);

  /**
   * Deletes basket history by basketId
   *
   * @param basketId
   * @param saleId
   * @param isAdmin
   * @param organisationId
   */
  void delete(Long basketId, Long saleId, Integer organisationId, boolean isAdmin, Long userId);

  /**
   * The service to count the total of basket histories by user.
   *
   * @param userId
   * @return the total basket histories
   */
  long countBasketHistoriesByUser(Long userId);

}
