package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

/**
 * The interface User create service.
 */
public interface UserCreateService {


  /**
   * Get eshop user profile.
   *
   * @param id             the id of user which get information
   * @param isOtherProfile the is other profile
   * @return {@link UserProfileDto}
   */
  UserProfileDto getUserProfile(long id, boolean isOtherProfile);

  UserProfileDto getUserProfile(UserInfo userInfo, boolean isOtherProfile);

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
