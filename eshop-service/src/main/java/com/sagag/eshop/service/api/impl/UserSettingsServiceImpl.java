package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.InvoiceTypeService;
import com.sagag.eshop.service.api.UserSettingsCreateService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.api.inner.UserSettingDtoBuilder;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.helper.UserSettingHelper;
import com.sagag.eshop.service.utils.EshopRoleUtils;
import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.CustomerSettingsDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.domain.sag.erp.Address;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation class for user settings services.
 */
@Service
@Transactional
@Slf4j
public class UserSettingsServiceImpl implements UserSettingsService, UserSettingsCreateService {

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Autowired
  private InvoiceTypeService invoiceTypeService;

  @Autowired
  private AddressFilterService axAddressFilterService;

  @Autowired
  private EshopRoleRepository eshopRoleRepository;

  @Autowired
  private CountryConfiguration countryConfig;

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Override
  public void updateShowHappyPointForUser(EshopUser eshopUser, EshopGroup eshopGroup) {
    final String initialRoleName = eshopRoleRepository.findRolesFromUserId(eshopUser.getId()).get(0);
    final String currentRoleName = eshopGroup.getGroupRoles().get(0).getEshopRole().getName();
    boolean isDowngradeToNormal = initialRoleName.equals(EshopAuthority.USER_ADMIN.name())
        && currentRoleName.equals(EshopAuthority.NORMAL_USER.name());
    boolean isUpgradeToAdmin = currentRoleName.equals(EshopAuthority.USER_ADMIN.name())
        && initialRoleName.equals(EshopAuthority.NORMAL_USER.name());
    if (isUpgradeToAdmin || isDowngradeToNormal) {
      UserSettings userSettings = eshopUser.getUserSetting();
      userSettings.setShowHappyPoints(isUpgradeToAdmin);
      userSettingsRepo.save(userSettings);
    }
  }

  @Override
  public Optional<UserSettings> findUserSettingsById(int id) {
    log.debug("Getting organisation order settings by id={}", id);
    return userSettingsRepo.findOneById(id);
  }

  @Override
  public UserSettings updateUserSettings(UserSettings userSettings) {
    return userSettingsRepo.save(userSettings);
  }

  @Override
  public UserSettings getSettingsByUserId(Long userId) {
    return userSettingsRepo.findSettingsByUserId(userId);
  }

  @Override
  public UserSettingsDto getUserSettings(final CustomerSettings customerSettings, final long userId,
      String roleName) {
    final UserSettings userSettings = getSettingsByUserId(userId);
    syncShowHappyPointWithCustomer(EnumUtils.getEnum(EshopAuthority.class, roleName), userSettings, customerSettings);
    if (EshopRoleUtils.isNormalUserRole(EshopAuthority.valueOf(roleName))) {
      syncUserSettingsWithCustomerSettings(userSettings, customerSettings);
    }
    UserSettingsDto userSettingsDto = UserSettingDtoBuilder.buildUserSettingsDto(userSettings, customerSettings, roleName);
    if (EshopAuthority.FINAL_NORMAL_USER.name().equals(roleName)
        || EshopAuthority.FINAL_USER_ADMIN.name().equals(roleName)) {
      setOwnDeliveryId(userId, userSettingsDto);
    }
    return userSettingsDto;
  }

  private void setOwnDeliveryId(final long userId, UserSettingsDto userSettingsDto) {
    CustomerSettingsDto ownCustomerSetting = customerSettingsService.getCustomerSetting(userId);
    userSettingsDto.setDeliveryId(ownCustomerSetting.getDeliveryId());
  }

  private UserSettings syncUserSettingsWithCustomerSettings(final UserSettings userSettings,
      final CustomerSettings customerSettings) {
    log.debug("Start sync user settings with customer settings");

    userSettings.setViewBilling(customerSettings.isViewBilling() && userSettings.isViewBilling());

    userSettings
        .setNetPriceView(customerSettings.isNetPriceView() && userSettings.isNetPriceView());

    userSettings.setCurrentStateNetPriceView(
        customerSettings.isNetPriceView() && userSettings.isCurrentStateNetPriceView());

    userSettings.setNetPriceConfirm(customerSettings.isNetPriceView()
        && customerSettings.isNetPriceConfirm() && userSettings.isNetPriceConfirm());
    userSettings.setShowDiscount(customerSettings.isNetPriceView()
        && customerSettings.isShowDiscount() && userSettings.isShowDiscount());

    return updateUserSettings(userSettings);
  }

  @Override
  @Transactional
  public UserSettings clone(UserSettings userSettings) {
    return UserSettings.builder()
        .allocationId(userSettings.getAllocationId())
        .collectiveDelivery(userSettings.getCollectiveDelivery())
        .deliveryId(userSettings.getDeliveryId())
        .paymentMethod(userSettings.getPaymentMethod())
        .viewBilling(userSettings.isViewBilling())
        .netPriceConfirm(userSettings.isNetPriceConfirm())
        .showDiscount(userSettings.isShowDiscount())
        .emailNotificationOrder(userSettings.isEmailNotificationOrder())
        .deliveryAddressId(userSettings.getDeliveryAddressId())
        .billingAddressId(userSettings.getBillingAddressId())
        .acceptHappyPointTerm(userSettings.isAcceptHappyPointTerm())
        .saleOnBehalfOf(userSettings.isSaleOnBehalfOf())
        .classicCategoryView(userSettings.getClassicCategoryView())
        .singleSelectMode(userSettings.getSingleSelectMode())
        .netPriceView(userSettings.isNetPriceView())
        .addressId(userSettings.getAddressId())
        .invoiceType(userSettings.getInvoiceType())
        .currentStateNetPriceView(userSettings.isCurrentStateNetPriceView())
        .showRecommendedRetailPrice(userSettings.isShowRecommendedRetailPrice())
        .build();
  }

  @Override
  public UserSettings createUserSetting(UserInfo user, CustomerSettings customerSettings, UserProfileDto userProfile) {
    // Create user setting
    final UserSettings userSettings =
        UserSettingHelper.buildDefaultUserSettingFromCustomerSetting(customerSettings);
    axAddressFilterService.filterAddress(user.getAddresses(), StringUtils.EMPTY, ErpAddressType.INVOICE)
        .map(Address::getId)
        .ifPresent(userSettings::setBillingAddressId);
    axAddressFilterService.filterAddress(user.getAddresses(), StringUtils.EMPTY, ErpAddressType.DELIVERY)
        .map(Address::getId)
        .ifPresent(userSettings::setDeliveryAddressId);

    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    userSettings.buildDefaultSettings(affiliate);
    userSettings.setNetPriceConfirm(countryConfig.getCustomerNetPriceConfirmSetting());
    userSettings.setNetPriceView(countryConfig.getCustomerNetPriceViewSetting());
    invoiceTypeService
        .getInvoiceTypeByCode(StringUtils.defaultIfBlank(user.getCustomer().getInvoiceTypeCode(),
            customerSettings.getInvoiceType().getInvoiceTypeCode()))
        .ifPresent(userSettings::setInvoiceType);
    return userSettingsRepo.save(userSettings);
  }

}
