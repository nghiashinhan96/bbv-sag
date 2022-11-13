package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

public interface UserSettingsCreateService {

  UserSettings createUserSetting(UserInfo user, CustomerSettings customerSettings,
      final UserProfileDto userProfile);
}
