package com.sagag.eshop.service.api.inner;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.utils.EshopRoleUtils;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.PriceSettingDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class UserSettingDtoBuilder {
  public static UserSettingsDto buildUserSettingsDto(final UserSettings userSettings,
      final CustomerSettings customerSetting, final String roleName) {
    final EshopAuthority eshopRole = EshopAuthority.valueOf(roleName);
    // @formatter: off
    final UserSettingsDto userSettingDto = UserSettingsDto.builder()
        .id(userSettings.getId())
        .allocationId(userSettings.getAllocationId())
        .paymentId(userSettings.getPaymentMethod().getId())
        .invoiceId(userSettings.getInvoiceType().getId())
        .deliveryId(userSettings.getDeliveryId())
        .collectiveDelivery(userSettings.getCollectiveDelivery())
        // #3546: "Faktura anzeigen" checkbox in the user settings of Customer admin is always
        // disabled and checked.
        .viewBilling(EshopRoleUtils.isUserAdminRole(eshopRole) ? true : userSettings.isViewBilling())
        .netPriceView(userSettings.isNetPriceView())
        .netPriceConfirm(userSettings.isNetPriceConfirm())
        .showDiscount(userSettings.isShowDiscount())
        .billingAddressId(userSettings.getBillingAddressId())
        .deliveryAddressId(userSettings.getDeliveryAddressId())
        .emailNotificationOrder(userSettings.isEmailNotificationOrder())
        .classicCategoryView(userSettings.getClassicCategoryView())
        .singleSelectMode(userSettings.getSingleSelectMode()).build();
    // @formatter: on
    userSettingDto.setPriceSettings(
        getPriceSettingsDefault(eshopRole, customerSetting));
    return userSettingDto;
  }

  private PriceSettingDto getPriceSettingsDefault(final EshopAuthority userRole,
      final CustomerSettings customerSettings) {
    log.debug("Start update price settings by role");
    if (EshopRoleUtils.isUserAdminRole(userRole)) {
      return PriceSettingDto.builder()
          // #3546: "Faktura anzeigen" checkbox in the user settings of Customer admin is always
          // disabled and checked.
          .allowViewBillingChanged(false)
          .allowNetPriceChanged(true)
          .allowNetPriceConfirmChanged(true)
          .build();
    }
    if (EshopRoleUtils.isFinalUserRole(userRole)) {
      return PriceSettingDto.builder()
          // #4855
          .allowViewBillingChanged(customerSettings.isViewBilling())
          .allowNetPriceChanged(false)
          .allowNetPriceConfirmChanged(false)
          .build();
    }
    return PriceSettingDto.builder()
        .allowViewBillingChanged(customerSettings.isViewBilling())
        .allowNetPriceChanged(customerSettings.isNetPriceView())
        .allowNetPriceConfirmChanged(customerSettings.isNetPriceConfirm())
        .build();
  }
}
