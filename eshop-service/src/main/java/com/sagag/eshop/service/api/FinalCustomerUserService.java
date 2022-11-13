package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.finaluser.FinalUserSearchCriteria;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerUserDto;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import org.springframework.data.domain.Page;

public interface FinalCustomerUserService {

  /**
   * Returns final user list which matched searchCriteria.
   *
   * @param finalCustomerId
   * @param criteria
   * @return Page of {@link FinalCustomerUserDto}
   */
  Page<FinalCustomerUserDto> searchFinalUsersBelongToFinalCustomer(Integer finalCustomerId,
      FinalUserSearchCriteria criteria);

  /**
   * Get eshop user profile.
   *
   * @param id the id of user which get information
   * @return {@link UserProfileDto}
   */
  UserProfileDto getUserProfile(long id);

  UserProfileDto getUserProfile(UserInfo userInfo, Integer finalCustomerId);

  /**
   * update eshop user profile.
   *
   * @param userProfile UserProfileDto of current user
   * @param user        UserInfo
   * @return {@link EshopUser}
   */
  EshopUser updateUserProfile(UserProfileDto userProfile, UserInfo user, boolean isOtherProfile)
      throws ValidationException;

}
