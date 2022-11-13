package com.sagag.services.service.user.setting;

import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;

import org.springframework.stereotype.Component;

@Component
public class FinalUserSyncAxSetting implements AxSettingsSynchronizer {

  @Override
  public void syncAxSetting(UserInfo user) {
    OwnSettings ownSettings = user.getSettings();
    ownSettings.setPriceDisplayChanged(false);
    ownSettings.setShowDiscount(false);
    ownSettings.setShowGross(false);
    if (user.getSupportedAffiliate().isSagCzAffiliate()) {
      ownSettings.setPriceTypeDisplayEnum(PriceDisplayTypeEnum.DPC);
    } else {
      ownSettings.setPriceTypeDisplayEnum(PriceDisplayTypeEnum.UVPE_OEP);
    }
  }

  @Override
  public boolean take(UserInfo user) {
    return user.isFinalUserRole();
  }

}
