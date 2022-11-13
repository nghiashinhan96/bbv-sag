package com.sagag.services.service.api;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.BasketHistoryDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.service.request.BasketHistoryRequestBody;
import com.sagag.services.service.request.BasketHistorySearchRequest;

import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Interface to define services for saved basket processing.
 */
public interface BasketService {

  /**
   * Returns basket histories by user login.
   *
   * <pre>
   * Support search basket histories for user login, sales and sales on behalf
   * </pre>
   *
   * @param request the search request
   * @param user who requests
   * @return the page of {@link BasketHistoryDto}
   *
   */
  Page<BasketHistoryDto> getBasketHistories(BasketHistorySearchRequest request, UserInfo user);

  /**
   * Creates basket history by user login.
   *
   * <pre>
   * Support create basket for user login and sales on behalf, not for sales
   * </pre>
   *
   * @param request the create basket request
   * @param user who requests
   * @return the created object of {@link BasketHistoryDto}
   *
   */
  void createBasketHistory(BasketHistoryRequestBody request, UserInfo user, ShopType shopType);

  /**
   * Counts basket histories by user login.
   *
   * <pre>
   * Support count basket histories for user login, sales and sales on behalf
   * </pre>
   *
   * @param customerNr the customer number
   * @param salesId the sales onbehalf id, if any
   * @param userId the current user login id
   * @return the value of {@link long}
   *
   */
  long countBasketHistoriesByUser(String customerNr, Long salesId, Long userId);

  /**
   * Gets the details of basket history by basket id.
   *
   * @param basketId the id of basket history
   * @return the detail of basket history
   *
   */
  Optional<BasketHistoryDto> getBasketHistoryDetails(final Long basketId);

  /**
   * Deletes basket history by basketId.
   *
   * @param user
   * @param basketId
   */
  void delete(final UserInfo user, final Long basketId);
}
