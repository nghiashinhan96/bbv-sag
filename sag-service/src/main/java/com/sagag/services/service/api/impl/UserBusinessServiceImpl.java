package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.InvoiceTypeService;
import com.sagag.eshop.service.api.PaymentMethodService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.api.inner.UserSettingDtoBuilder;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.BackOfficeUserProfileDto;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.SettingUpdateDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.user.cache.ISyncUserLoader;
import com.sagag.services.service.user.handler.BackOfficeManageDataByAdminHandler;
import com.sagag.services.service.user.handler.EshopManageDataByAdminHandler;
import com.sagag.services.service.user.handler.EshopSelfManageDataHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserBusinessServiceImpl implements UserBusinessService {

  @Autowired
  private UserCacheService userCacheService;

  @Autowired
  private InvoiceTypeService invoiceTypeService;

  @Autowired
  private PaymentMethodService paymentMethodService;

  @Autowired
  private CustomerSettingsService custSettingsService;

  @Autowired
  private UserSettingsService userSettingsService;

  @Autowired
  private EshopSelfManageDataHandler eshopSelfManageDataHandler;

  @Autowired
  private EshopManageDataByAdminHandler eshopManageDataByAdminHandler;

  @Autowired
  private BackOfficeManageDataByAdminHandler backOfficeManageDataByAdminHandler;

  @Autowired
  private ISyncUserLoader syncUserLoader;

  @Autowired
  private AddressFilterService addressFilterService;

  @Override
  public void createUserByAdmin(UserInfo adminUser, UserProfileDto userProfile,
      OrganisationDto target) throws ValidationException, MdmCustomerNotFoundException {
    eshopManageDataByAdminHandler.createUser(adminUser, userProfile, target);
  }

  @Override
  public void createUserByAdmin(UserInfo adminUser, UserProfileDto userProfile)
      throws ValidationException, MdmCustomerNotFoundException {
    eshopManageDataByAdminHandler.createUser(adminUser, userProfile);
  }

  @Override
  public void createUserBySystemAdmin(BackOfficeUserProfileDto userProfile)
      throws ValidationException, MdmCustomerNotFoundException {
    backOfficeManageDataByAdminHandler.createUserBySystemAdmin(userProfile);
  }

  @Override
  public void deactiveUserById(final Long deletedUserId) throws UserValidationException {
    eshopManageDataByAdminHandler.deactiveUserById(deletedUserId);
  }

  @Override
  public void deleteDvseExternalUserById(final Long deletedUserId)
      throws UserValidationException, MdmCustomerNotFoundException {
    eshopManageDataByAdminHandler.deleteDvseExternalUserById(deletedUserId);
  }

  @Override
  public PaymentSettingDto getUserPaymentSetting(UserInfo user) {
    return eshopSelfManageDataHandler.getPaymentSetting(user);
  }

  @Override
  public PaymentSettingDto filterUserPaymentSetting(UserInfo user) {
    PaymentSettingDto paymentSetting = eshopSelfManageDataHandler.getPaymentSetting(user);
    filterUserPaymentSetting(paymentSetting);
    return paymentSetting;
  }

  @Override
  public PaymentSettingDto getUserPaymentSettingByAdmin(UserInfo user, Long requestUserId) {
    return eshopManageDataByAdminHandler.getPaymentSetting(user, requestUserId);
  }

  @Override
  public UserSettingsDto getUserSettings(UserInfo user) {
    UserSettingsDto userSettingsDto = eshopSelfManageDataHandler.getUserSettings(user);
    updateCurrentSessionSettings(userSettingsDto, user);
    return userSettingsDto;
  }

  @Override
  public UserSettingsDto getUserSettingsByAdmin(UserInfo user, Long requestUserId) {
    return eshopManageDataByAdminHandler.getUserSettings(user, requestUserId);
  }

  @Override
  public String getUserRoleName(long userId) {
    return eshopManageDataByAdminHandler.getUserRoleName(userId);
  }

  @Override
  @LogExecutionTime
  public UserInfo findCacheUser(long currentLoggedUserId, String loginAffiliate, String clientId) {
    return syncUserLoader.load(currentLoggedUserId, loginAffiliate, clientId, Optional.empty());
  }

  @Override
  public UserInfo findCacheUser(long currentLoggedUserId, String loginAffiliate, String clientId,
      Optional<Long> saleIdOpt) {
    return syncUserLoader.load(currentLoggedUserId, loginAffiliate, clientId, saleIdOpt);
  }

  @Override
  public void clearCacheUser(final Long clearUserId) {
    userCacheService.remove(clearUserId);
  }

  @Override
  public UserSettingsDto updateMyUserSettings(SettingUpdateDto settingsUpdateDto, UserInfo user) {
    settingsUpdateDto.setUserId(user.getId());
    UserSettingsDto userSettingsDto =
        updateUserSettings(settingsUpdateDto, user.getOrganisationId(), user.getRoleName());
    updateCurrentSessionSettings(userSettingsDto, user);
    return userSettingsDto;
  }

  @Override
  public UserSettingsDto updateUserSettingsByAdmin(SettingUpdateDto settingsUpdateDto, int organisationId,
      String roleName) {
    return updateUserSettings(settingsUpdateDto, organisationId, roleName);
  }

  private UserSettingsDto updateUserSettings(SettingUpdateDto settingsUpdateDto, int organisationId,
      String roleName) {
    Optional<InvoiceType> invoiceType =
        invoiceTypeService.getInvoiceTypeById(settingsUpdateDto.getInvoiceId());
    Optional<PaymentMethod> paymentMethod =
        paymentMethodService.getPaymentMethodById(settingsUpdateDto.getPaymentId());
    if (!invoiceType.isPresent() || !paymentMethod.isPresent()) {
      throw new IllegalArgumentException("Invalid invoice type or payment method");
    }

    final Long userId = settingsUpdateDto.getUserId();
    UserSettings userSettings = userSettingsService.getSettingsByUserId(userId);
    if (Objects.isNull(userSettings)) {
      throw new IllegalArgumentException("Invalid user setting");
    }
    userSettings.setAddressId(settingsUpdateDto.getAddressId());
    userSettings.setAllocationId(settingsUpdateDto.getAllocationId());
    userSettings.setCollectiveDelivery(settingsUpdateDto.getCollectiveDelivery());
    userSettings.setDeliveryId(settingsUpdateDto.getDeliveryId());
    userSettings.setPaymentMethod(paymentMethod.get());
    userSettings.setBillingAddressId(settingsUpdateDto.getBillingAddressId());
    userSettings.setDeliveryAddressId(settingsUpdateDto.getDeliveryAddressId());
    userSettings.setViewBilling(settingsUpdateDto.isViewBilling());
    userSettings.setEmailNotificationOrder(settingsUpdateDto.isEmailNotificationOrder());
    userSettings.setNetPriceView(settingsUpdateDto.isNetPriceView());
    userSettings.setShowDiscount(userSettings.isNetPriceView() && userSettings.isShowDiscount());
    userSettings.setCurrentStateNetPriceView(settingsUpdateDto.isNetPriceView());
    userSettings.setNetPriceConfirm(settingsUpdateDto.isNetPriceConfirm());
    userSettings.setInvoiceType(invoiceType.get());
    userSettings.setClassicCategoryView(settingsUpdateDto.isClassicCategoryView());
    userSettings.setSingleSelectMode(settingsUpdateDto.isSingleSelectMode());

    final CustomerSettings customerSettings =
        custSettingsService.findSettingsByOrgId(organisationId);

    userSettingsService.updateUserSettings(userSettings);
    clearCacheUser(userId);
    return UserSettingDtoBuilder.buildUserSettingsDto(userSettings, customerSettings, roleName);
  }

  private void updateCurrentSessionSettings(UserSettingsDto userSettingsDto, UserInfo user) {
    userSettingsDto.setShowDiscount(user.getSettings().isShowDiscount());
    userSettingsDto.setShowGross(user.getSettings().isShowGross());
    userSettingsDto.setPriceDisplayChanged(user.getSettings().isPriceDisplayChanged());
  }

  private void filterUserPaymentSetting(PaymentSettingDto paymentSetting) {
    addressFilterService.getDeliveryAddresses(paymentSetting.getAddresses());
  }

}
