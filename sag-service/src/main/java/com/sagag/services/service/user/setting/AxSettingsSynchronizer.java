package com.sagag.services.service.user.setting;

import com.sagag.eshop.service.dto.UserInfo;

public interface AxSettingsSynchronizer {

  void syncAxSetting(UserInfo user);

  boolean take(UserInfo user);

}
