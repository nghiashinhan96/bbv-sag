package com.sagag.eshop.service.helper;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.services.common.contants.UserSettingConstants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserSettingHelper {

  public static UserSettings buildDefaultUserSettingFromCustomerSetting(
      CustomerSettings customerSettings) {
    final UserSettings userSettings = new UserSettings();
    userSettings.setAllocationId(customerSettings.getAllocationId());
    userSettings.setCollectiveDelivery(customerSettings.getCollectiveDelivery());
    userSettings.setDeliveryId(customerSettings.getDeliveryId());
    userSettings.setPaymentMethod(customerSettings.getPaymentMethod());
    userSettings.setInvoiceType(customerSettings.getInvoiceType());
    userSettings.setViewBilling(customerSettings.isViewBilling());
    userSettings.setCurrentStateNetPriceView(customerSettings.isNetPriceView());
    userSettings.setShowDiscount(customerSettings.isShowDiscount());
    userSettings.setEmailNotificationOrder(customerSettings.isEmailNotificationOrder());
    userSettings.setClassicCategoryView(UserSettingConstants.CLASSIC_CATEGORY_VIEW_DEFAULT);
    userSettings.setSingleSelectMode(UserSettingConstants.SINGLE_SELECT_MODE_DEFAULT);
    return userSettings;
  }
}
