package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.criteria.finaluser.FinalUserSearchCriteria;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.repo.specification.FinalUserSearchSpecifications;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.FinalCustomerUserService;
import com.sagag.eshop.service.api.InvoiceTypeService;
import com.sagag.eshop.service.api.UserCreateService;
import com.sagag.eshop.service.api.UserSettingsCreateService;
import com.sagag.eshop.service.converter.FinalCustomerUserConverters;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerUserDto;
import com.sagag.eshop.service.helper.UserSettingHelper;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.function.Function;

@Service
public class FinalCustomerUserServiceImpl extends AbstractUserCreation implements UserSettingsCreateService,
    FinalCustomerUserService {

  private static final String NET_PRICE_MESSAGE = "Final customer user must have net price config";
  private static final String NET_PRICE_CONFIRM_MESSAGE = "Final customer user must have net price confirm config";

  @Autowired
  private EshopUserRepository userRepository;

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Autowired
  private InvoiceTypeService invoiceTypeService;

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Autowired
  private UserCreateService userCreateService;

  @Autowired
  private AddressFilterService axAddressFilterService;

  @Override
  public UserProfileDto getUserProfile(long userId) {
    return userRepository.findById(userId)
        .map(eshopUser -> userProfileInfoConverter()
            .andThen(languageInfoConverter(eshopUser))
            .andThen(salutationInfoConverter(eshopUser))
            .andThen(roleTypesInfoConverter(eshopUser, true))
            .andThen(additionalInfoConverter())
            .andThen(settingConverter(userId))
            .apply(eshopUser))
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

  @Override
  public UserProfileDto getUserProfile(UserInfo userInfo, Integer finalCustomerId) {
    return userRepository.findById(userInfo.getId())
        .map(eshopUser -> userProfileInfoConverter()
            .andThen(languageInfoConverter(eshopUser))
            .andThen(salutationInfoConverter(eshopUser))
            .andThen(roleTypesInfoConverter(eshopUser, false))
            .andThen(additionalInfoConverter())
            .andThen(
                settingConverter(finalCustomerId, userInfo.getOrganisationId()))
            .apply(eshopUser))
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

  @Override
  public EshopUser updateUserProfile(UserProfileDto userProfile, UserInfo user, boolean isOtherProfile)
      throws ValidationException {
    userProfile.setNetPriceView(BooleanUtils.isTrue(userProfile.getNetPriceView()));
    userProfile.setNetPriceConfirm(BooleanUtils.isTrue(userProfile.getNetPriceConfirm()));
    userProfile.setEmailConfirmation(BooleanUtils.isTrue(userProfile.getEmailConfirmation()));
    // update User profile like normal user
    EshopUser updatedUser = userCreateService.updateUserProfile(userProfile, user, isOtherProfile);

    // update setting for final user
    userSettingsRepo.findByUserId(updatedUser.getId()).ifPresent(setting -> {
      setting.setNetPriceView(userProfile.getNetPriceView());
      setting.setNetPriceConfirm(userProfile.getNetPriceConfirm());
      setting.setEmailNotificationOrder(userProfile.getEmailConfirmation());
      setting.setCurrentStateNetPriceView(userProfile.getNetPriceView());
      userSettingsRepo.save(setting);
    });
    return updatedUser;
  }

  private Function<UserProfileDto, UserProfileDto> settingConverter(Integer finalCustomerOrgId, Integer wholesalerOrgId) {
    return profile -> {
      CustomerSettings wssCustomerSettings = customerSettingsService.findSettingsByOrgId(wholesalerOrgId);
      profile.setNetPriceConfirm(wssCustomerSettings.isNetPriceConfirm());
      profile.setNetPriceView(wssCustomerSettings.isNetPriceView());
      boolean showNetPriceEnabled = true;
      if (finalCustomerOrgId != null) {
        CustomerSettings finalCustomerSettings = customerSettingsService.findSettingsByOrgId(finalCustomerOrgId);
        profile.setNetPriceConfirm(finalCustomerSettings.isNetPriceConfirm());
        profile.setNetPriceView(finalCustomerSettings.isNetPriceView());
        showNetPriceEnabled = wssCustomerSettings.isWssShowNetPrice() && finalCustomerSettings.isNetPriceView();
      }
      profile.setShowNetPriceEnabled(showNetPriceEnabled);
      return profile;
    };
  }

  @Override
  protected Function<UserProfileDto, UserProfileDto> roleTypesInfoConverter(EshopUser eshopUser,
      boolean isOtherProfile) {
    return profile -> {
      final String mainUserRole =
          EshopUserCreateAuthority.switchToFinalRole(EshopUserCreateAuthority.valueOf(eshopUser.getRoles().get(0)));
      profile.setTypes(roleService.getEshopRolesForUserProfile(mainUserRole, isOtherProfile));
      // Set own role
      roleService.findRoleByName(mainUserRole).ifPresent(
          userRole -> profile.setTypeId(userRole.getId()));
      return profile;
    };
  }

  private Function<UserProfileDto, UserProfileDto> settingConverter(long userId) {
    return profile -> {
      UserSettings settings = userSettingsRepo.findSettingsByUserId(userId);
      profile.setNetPriceConfirm(settings.isNetPriceConfirm());
      profile.setNetPriceView(settings.isNetPriceView());
      profile.setEmailConfirmation(settings.isEmailNotificationOrder());
      return profile;
    };
  }

  @Override
  public UserSettings createUserSetting(UserInfo user, CustomerSettings customerSettings, UserProfileDto userProfile) {
    Assert.notNull(userProfile.getNetPriceView(), NET_PRICE_MESSAGE);
    Assert.notNull(userProfile.getNetPriceConfirm(), NET_PRICE_CONFIRM_MESSAGE);
    // Create user setting
    final UserSettings userSettings = UserSettingHelper.buildDefaultUserSettingFromCustomerSetting(customerSettings);
    axAddressFilterService.filterAddress(user.getAddresses(), StringUtils.EMPTY, ErpAddressType.INVOICE)
        .map(Address::getId)
        .ifPresent(userSettings::setBillingAddressId);
    axAddressFilterService.filterAddress(user.getAddresses(), StringUtils.EMPTY, ErpAddressType.DELIVERY)
        .map(Address::getId)
        .ifPresent(userSettings::setDeliveryAddressId);
    userSettings.setNetPriceView(userProfile.getNetPriceView());
    userSettings.setNetPriceConfirm(userProfile.getNetPriceConfirm());
    userSettings.setEmailNotificationOrder(userProfile.getEmailConfirmation());
    userSettings.setCurrentStateNetPriceView(userProfile.getNetPriceView());
    userSettings.setShowDiscount(false);
    invoiceTypeService
        .getInvoiceTypeByCode(StringUtils.defaultIfBlank(user.getCustomer().getInvoiceTypeCode(),
            customerSettings.getInvoiceType().getInvoiceTypeCode()))
        .ifPresent(userSettings::setInvoiceType);
    return userSettingsRepo.save(userSettings);
  }

  @Override
  public Page<FinalCustomerUserDto> searchFinalUsersBelongToFinalCustomer(Integer finalCustomerId,
      FinalUserSearchCriteria criteria) {
    Specification<VUserDetail> spec =
        FinalUserSearchSpecifications.searchUserListByFinalCustomer(criteria, finalCustomerId);
    Pageable pageable = criteria.buildPageable();
    Page<VUserDetail> result = vUserDetailRepo.findAll(spec, pageable);
    return result.map(FinalCustomerUserConverters.converter());
  }

}
