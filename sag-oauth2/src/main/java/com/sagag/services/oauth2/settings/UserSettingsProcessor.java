package com.sagag.services.oauth2.settings;

import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserSettingsProcessor implements SettingsProcessor<EshopUser, UserSettings> {

  private final UserSettingsRepository userSettingsRepo;

  private final UserSettingsService userSettingsService;

  private final CustomerSettingsProcessor customerSettingsProcessor;

  private final CustomerCacheService customerCacheService;

  private final AddressFilterService axAddressFilterService;

  @Autowired
  public UserSettingsProcessor(UserSettingsRepository userSettingsRepo,
    UserSettingsService userSettingsService,
    CustomerSettingsProcessor customerSettingsProcessor,
    CustomerCacheService customerCacheService,
    AddressFilterService axAddressFilterService) {
    this.userSettingsRepo = userSettingsRepo;
    this.userSettingsService = userSettingsService;
    this.customerSettingsProcessor = customerSettingsProcessor;
    this.axAddressFilterService = axAddressFilterService;
    this.customerCacheService = customerCacheService;
  }

  @Override
  public Optional<UserSettings> process(EshopUser user) {
    UpdatedCustomerSettingsDto updatedCustSettingsDto = customerSettingsProcessor.process(user)
        .orElseThrow(() -> new IllegalStateException(
            "customer setting process was failed and illegal state."));

    final CustomerSettings cusSettings = updatedCustSettingsDto.getCustomerSettings();
    log.debug("Customer Settings: {}", cusSettings);
    UserSettings userSettings = user.getUserSetting();
    updatePriceSettingsByRole(cusSettings, userSettings, user);

    // #2718
    PaymentMethodType paymentMethodType =
        PaymentMethodType.valueOf(cusSettings.getPaymentMethod().getDescCode());
    if (paymentMethodType.isCash() || paymentMethodType.isCredit()) {
      userSettings.setPaymentMethod(cusSettings.getPaymentMethod());
    }
    userSettings.setDeliveryId(cusSettings.getDeliveryId());
    userSettings.setInvoiceType(cusSettings.getInvoiceType());
    userSettingsService.syncShowHappyPointWithCustomer(
        EnumUtils.getEnum(EshopAuthority.class, user.getRoles().get(0)), userSettings, cusSettings);

    Customer customer = updatedCustSettingsDto.getCustomerOptional()
        .orElseThrow(IllegalStateException::new);

    final String custNr = String.valueOf(customer.getNr());
    final SupportedAffiliate affiliate =
        SupportedAffiliate.fromDesc(customer.getAffiliateShortName());
    List<Address> addresses = customerCacheService.getCachedCustomerAddresses(custNr,
        affiliate.getCompanyName());
    axAddressFilterService.filterAddress(addresses, userSettings.getBillingAddressId(), ErpAddressType.INVOICE)
        .map(Address::getId)
        .ifPresent(userSettings::setBillingAddressId);

    axAddressFilterService.filterAddress(addresses, userSettings.getDeliveryAddressId(),
        ErpAddressType.DELIVERY)
        .map(Address::getId)
        .ifPresent(userSettings::setDeliveryAddressId);
    userSettingsRepo.save(userSettings);

    return Optional.of(userSettings);
  }

  private void updatePriceSettingsByRole(CustomerSettings cusSettings, UserSettings userSettings,
      EshopUser user) {
    if (user.isNormalUserRole()) {
      userSettings.setNetPriceView(cusSettings.isNetPriceView() && userSettings.isNetPriceView());
      userSettings.setNetPriceConfirm(userSettings.isNetPriceView()
          && cusSettings.isNetPriceConfirm() && userSettings.isNetPriceConfirm());
      userSettings.setShowDiscount(userSettings.isNetPriceView() && cusSettings.isShowDiscount()
          && userSettings.isShowDiscount());
    } else if (user.isUserAdminRole()) {
      userSettings.setShowDiscount(userSettings.isNetPriceView() && userSettings.isShowDiscount());
    }

    userSettings.setCurrentStateNetPriceView(
        userSettings.isNetPriceView() && userSettings.isCurrentStateNetPriceView());
  }

}
