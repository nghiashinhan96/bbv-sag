package com.sagag.services.service.payment.impl;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.service.payment.UserPaymentSettingsFormBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class AbstractUserPaymentSettingsFormBuilderImpl
  implements UserPaymentSettingsFormBuilder {

  @Autowired
  private CustomerSettingsRepository customerSettingsRepo;

  @Autowired
  private UserSettingsService userSettingsService;

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Autowired
  private UserService userService;

  @Override
  public PaymentSettingDto buildUserPaymentSetting(final Long userId, boolean salesOnBehalf) {
    Assert.notNull(userId, "The current user login must not be null");

    final Optional<VUserDetail> eshopUserDetailOpt = vUserDetailRepo.findByUserId(userId);
    if (!eshopUserDetailOpt.isPresent()) {
      final String msg = String.format("Not found any user info from user id = %s", userId);
      log.warn(msg);
      throw new IllegalArgumentException(msg);
    }
    final VUserDetail userDetail = eshopUserDetailOpt.get();
    final Integer custSettingsId = userDetail.getOrgSettingsId();
    final Integer userSettingsId = userDetail.getUserSettingId();

    final PaymentSettingDto originalPaymentSettings = userService.getPaymentSetting(salesOnBehalf, false);
    final CustomerSettings customerSettings =
        customerSettingsRepo.findById(custSettingsId).orElse(null);
    final Optional<UserSettings> userSettingsOpt =
        userSettingsService.findUserSettingsById(userSettingsId);
    if (!userSettingsOpt.isPresent()) {
      final String msg =
          String.format("Not found any user settings from user settings id = %s", userSettingsId);
      log.warn(msg);
      throw new IllegalArgumentException(msg);
    }
    if (Objects.isNull(customerSettings) || Objects.isNull(customerSettings.getPaymentMethod())) {
      throw new IllegalArgumentException("Payment method must not be null.");
    }
    return syncPaymentSettingWithCustomerSettings(originalPaymentSettings,
        customerSettings.getPaymentMethod().getDescCode());
  }

  private static PaymentSettingDto syncPaymentSettingWithCustomerSettings(
      final PaymentSettingDto originalPaymentSetting, final String descCode) {
    log.debug("Start sync payment settings with user settings");

    final PaymentSettingDto syncedPaymentSetting = originalPaymentSetting;
    // Sync the current setting to payment method setting
    final PaymentMethodType customerPaymentMethod = PaymentMethodType.valueOfIgnoreCase(descCode);
    final List<PaymentMethodDto> syncedPaymentMethods = new ArrayList<>();
    syncedPaymentSetting.getPaymentMethods().forEach(paymentMethod -> {
      final boolean allowChoose = customerPaymentMethod.isCredit()
          || !PaymentMethodType.valueOfIgnoreCase(paymentMethod.getDescCode()).isCredit();
      paymentMethod.setAllowChoose(allowChoose);
      syncedPaymentMethods.add(paymentMethod);
    });

    syncedPaymentSetting.setPaymentMethods(syncedPaymentMethods);

    return syncedPaymentSetting;
  }
}
