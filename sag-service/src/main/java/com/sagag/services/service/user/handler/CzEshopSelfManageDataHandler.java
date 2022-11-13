package com.sagag.services.service.user.handler;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.stakis.erp.enums.StakisSendMethodType;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@CzProfile
public class CzEshopSelfManageDataHandler extends EshopSelfManageDataHandler {

  @Override
  public UserSettingsDto getUserSettings(UserInfo userInfo) {
    UserSettingsDto userSettings = super.getUserSettings(userInfo);
    if (userInfo.isFinalUserRole()) {
      return userSettings;
    }
    Optional.ofNullable(userInfo).filter(user -> user.hasCust()).map(UserInfo::getCustomer)
        .ifPresent(cus -> userSettings.setDeliveryId(StakisSendMethodType
            .findBySendMethod(SendMethodType.valueOf(cus.getSendMethodCode())).getId()));
    return userSettings;
  }

}
