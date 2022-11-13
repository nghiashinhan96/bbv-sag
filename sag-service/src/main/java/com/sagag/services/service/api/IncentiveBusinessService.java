package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.domain.IncentivePointsDto;
import com.sagag.services.incentive.response.IncentiveLinkResponse;

import java.util.Optional;

/**
 * Interface to define incentive points services.
 */
public interface IncentiveBusinessService {

  /**
   * Returns the happy bonus link by user id.
   *
   * @param user the current user login
   * @return the happy bonus link
   */
  IncentiveLinkResponse getIncentiveUrl(UserInfo user) throws CookiePrivacyException;

  /**
   * Returns the happy point by customer.
   *
   * @param user
   * @return the optional of {@link IncentivePointsDto}
   */
  Optional<IncentivePointsDto> findHappyPointsByCustomer(UserInfo user);

  /**
   * Updates accept happy point term by userId.
   *
   * @param userId the current userId
   */
  void saveAccetpHappyPointTerm(Long userId);
}
