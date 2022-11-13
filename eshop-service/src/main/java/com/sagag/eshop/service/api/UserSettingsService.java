package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.utils.EshopRoleUtils;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;

import java.util.Optional;

/**
 * Interface to provide some services for user settings.
 */
public interface UserSettingsService {

  /**
   * Sync showHappyPoint userSetting from data of customer.
   *
   * @param roleName
   * @param userSettings
   * @param customerSettings
   */
  default UserSettings syncShowHappyPointWithCustomer(EshopAuthority roleName, UserSettings userSettings,
      CustomerSettings customerSettings) {
    userSettings.setShowHappyPoints(
        (EshopRoleUtils.isUserAdminRole(roleName) && customerSettings.isHasPartnerprogramView()) || (
            userSettings.isShowHappyPoints() && customerSettings.isHasPartnerprogramView()));
    return userSettings;
  }

  /**
   * Updates show happy point for user when downgrading/upgrading user role
   *
   * @param eshopUser
   * @param eshopGroup
   */
  void updateShowHappyPointForUser(EshopUser eshopUser, EshopGroup eshopGroup);

  /**
   * Returns a {@link UserSettings}.
   *
   * @return UserSettings.
   */
  Optional<UserSettings> findUserSettingsById(int id);

  /**
   * Update user settings.
   *
   * @param userSettings the userSettings to update
   * @return UserSettings
   */
  UserSettings updateUserSettings(final UserSettings userSettings);

  /**
   * Returns a {@link UserSettings}.
   *
   * @return UserSettings.
   */
  UserSettings getSettingsByUserId(Long userId);

  /**
   * Gets user setting dto.
   *
   * @param customerSettings
   * @param userId
   * @param roleName
   * @return UserSettingsDto object
   */
  UserSettingsDto getUserSettings(CustomerSettings customerSettings, long userId, String roleName);

  /**
   * Clones UserSettings.
   * It should have different Id
   * @param userSettings
   * @return
   */
  UserSettings clone(UserSettings userSettings);

}
