package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.BackOfficeUserProfileDto;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.SettingUpdateDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;

import java.util.Optional;

public interface UserBusinessService {

  /**
   * Creates a new user by admin.
   *
   * @param adminUser the admin user
   * @param userProfile the user profile to create
   */
  void createUserByAdmin(UserInfo adminUser, UserProfileDto userProfile)
      throws ValidationException, MdmCustomerNotFoundException;

  /**
   * Creates a new user by admin for another customer
   *
   * @param adminUser   the admin user
   * @param userProfile the user profile to create
   * @param target      the target customer to create user for
   */
  void createUserByAdmin(UserInfo adminUser, UserProfileDto userProfile, OrganisationDto target)
      throws ValidationException, MdmCustomerNotFoundException;

  /**
   * Creates a new user by system admin.
   *
   * @param userProfile
   * @throws ValidationException
   * @throws MdmCustomerNotFoundException
   */
  void createUserBySystemAdmin(BackOfficeUserProfileDto userProfile)
      throws ValidationException, MdmCustomerNotFoundException;

  /**
   * Deactives user login to prevent user can login to e-Connect system.
   *
   * @param deletedUserId the user id to delete
   */
  void deactiveUserById(final Long deletedUserId) throws UserValidationException;

  /**
   * Deletes Dvse and external user if affiliate is belongs to Austria.
   *
   * @param deletedUserId the user id to delete
   */
  void deleteDvseExternalUserById(final Long deletedUserId)
      throws UserValidationException, MdmCustomerNotFoundException;

  /**
   * Finds the user saved in cache.
   * <p>
   * if not found, that user will be synchronized and saved to cache. This could be performanced but
   * will fast for the second time.
   *
   * @param currentLoggedUserId the current logged user id
   * @param loginAffiliate
   * @param clientId
   * @return the user details in cache
   */
  UserInfo findCacheUser(long currentLoggedUserId, String loginAffiliate, String clientId);

  /**
   * Finds the user saved in cache.
   * <p>
   * if not found, that user will be synchronized and saved to cache. This could be performanced but
   * will fast for the second time.
   *
   * @param currentLoggedUserId the current logged user id
   * @param loginAffiliate
   * @param clientId
   * @param saleIdOpt
   * @return the user details in cache
   */
  UserInfo findCacheUser(long currentLoggedUserId, String loginAffiliate, String clientId,
      Optional<Long> saleIdOpt);

  /**
   * Clears the cached user detail.
   *
   * @param clearUserId the user id to clear
   */
  void clearCacheUser(final Long clearUserId);

  /**
   * Get payment setting for user by them own.
   *
   * @param user
   * @return
   */
  PaymentSettingDto getUserPaymentSetting(UserInfo user);

  /**
   * Get payment setting follow filtering condition for user by them own.
   *
   * @param user
   * @return
   */
  PaymentSettingDto filterUserPaymentSetting(UserInfo user);

  /**
   * Get payment setting for user by customer administrator.
   *
   * @param user
   * @param requestUserId
   * @return
   */
  PaymentSettingDto getUserPaymentSettingByAdmin(UserInfo user, Long requestUserId);

  /**
   * Gets user's settings.
   *
   * @param user
   * @return the settings of himself
   */
  UserSettingsDto getUserSettings(UserInfo user);

  /**
   * Gets user's settings by admin
   *
   * @param user
   * @param requestUserId
   * @return the settings of requestUserId
   */
  UserSettingsDto getUserSettingsByAdmin(UserInfo user, Long requestUserId);

  /**
   * Updates setting for other user by admin.
   *
   * @param settingsUpdateDto the setting info need to be updated
   * @param organisationId the organisation that user belong to
   * @param roleName role name of that user
   * @return updated setting
   */
  UserSettingsDto updateUserSettingsByAdmin(SettingUpdateDto settingsUpdateDto, int organisationId,
      String roleName);

  /**
   * Returns role name of user.
   *
   * @param userId id of user need to get role name
   * @return role name
   */
  String getUserRoleName(long userId);

  /**
   * Updates setting for userself.
   *
   * @param settingsUpdateDto the setting info need to be updated
   * @param user the login user info
   * @return updated setting
   */
  UserSettingsDto updateMyUserSettings(SettingUpdateDto settingsUpdateDto, UserInfo user);
}
