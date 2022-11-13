package com.sagag.services.service.user.handler;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.service.user.UserBusinessHelper;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * This class supports APIs for all users can interact itself.
 */
public class EshopSelfManageDataHandler extends AbstractUserHandler {

  @Autowired
  private UserBusinessHelper userBusinessHelper;

  @Autowired
  private AddressFilterService addressFilterService;

  public PaymentSettingDto getPaymentSetting(UserInfo user) {
    final PaymentSettingDto paymentSettings =
        userPaymentSettingsFormBuilder.buildUserPaymentSetting(user.getId(), user.isSaleOnBehalf());
    fillPaymentAddresses(paymentSettings, user);

    return paymentSettings;
  }

  public UserSettingsDto getUserSettings(UserInfo user) {
    CustomerSettings customerSettings =
        custSettingsService.findSettingsByOrgId(user.getOrganisationId());
    if (user.isSaleOnBehalf()) {
      customerSettings = userBusinessHelper.syncDeliveryTypeFromAX(user, customerSettings);
    }

    return userSettingsService.getUserSettings(customerSettings, user.getId(), user.getRoleName());
  }

  protected void fillPaymentAddresses(PaymentSettingDto paymentSettings, UserInfo user) {
    if (user.hasAddress()) {
      List<Address> addresses = user.getAddresses();
      addresses.forEach(address -> address.setAddressId(address.getId()));
      paymentSettings.setAddresses(addresses);
      paymentSettings.setBillingAddresses(addressFilterService.getBillingAddresses(addresses));
    }
  }
}
