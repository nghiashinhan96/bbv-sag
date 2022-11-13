package com.sagag.services.service.user.cache.impl;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.AadAccountsService;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.LegalTermService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.PaymentMethodService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.converter.OrganisationConverters;
import com.sagag.eshop.service.converter.WssDeliveryProfileConverters;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.enums.LinkPartnerEnum;
import com.sagag.eshop.service.utils.AffiliateSettingConstants;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.EshopGlobalSettingEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.domain.eshop.dto.AadAccountsSearchResultDto;
import com.sagag.services.domain.eshop.dto.OrderLocation;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.hazelcast.api.EshopClientCacheService;
import com.sagag.services.hazelcast.api.EshopGlobalSettingCacheService;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.hazelcast.domain.EshopGlobalSettingDto;
import com.sagag.services.mdm.utils.DvseUriBuilder;
import com.sagag.services.service.order.location.OrderLocationBuilder;
import com.sagag.services.service.user.cache.ISyncUserLoader;
import com.sagag.services.service.user.setting.AxSettingsSynchronizer;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Slf4j
public abstract class AbstractSyncUserLoader implements ISyncUserLoader {

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Autowired
  private CustomerCacheService customerCacheService;

  @Autowired
  private CustomerExternalService customerExtService;

  @Autowired
  protected OrganisationService orgService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserCacheService userCacheService;

  @Autowired
  private CustomerSettingsService custSettingsService;

  @Autowired
  private UserSettingsService userSettingsService;

  @Autowired
  private EshopClientCacheService clientDetailCacheService;

  @Autowired
  private List<AxSettingsSynchronizer> synAxSettings;

  @Autowired(required = false)
  private OrderLocationBuilder orderLocationBuilder;

  @Autowired
  private PaymentMethodService paymentMethodService;

  @Autowired
  private AadAccountsService aadAccountsService;

  @Autowired
  @Qualifier("eshopGlobalSettingCacheServiceImpl")
  private EshopGlobalSettingCacheService eshopGlobalSettingCacheService;

  @Autowired
  private LegalTermService legalTermService;

  @Override
  @Transactional
  @LogExecutionTime
  public UserInfo load(long currentLoggedUserId, String loginAffiliate, String clientId,
      Optional<Long> saleIdOpt) {
    log.debug("Syncing the connect user into user cache by user_id = {} - client_id = {}",
        currentLoggedUserId, clientId);
    UserInfo userInfo = userCacheService.get(currentLoggedUserId);
    if (!Objects.isNull(userInfo)) {
      log.debug("Getting user from cache");
      loadAlwaysCustomUserSetting(userInfo, loginAffiliate, clientId, saleIdOpt);
      return userInfo;
    }
    log.debug("The user was not found in cache, getting a new one");

    // Prepare localized DB user details
    userInfo = userService.getSessionUserInfo(currentLoggedUserId);

    if (userInfo.isSalesUser()) {
      final Optional<AadAccountsSearchResultDto> saleAadAccountOpt =
          aadAccountsService.findByUserId(userInfo.getId());
      if (saleAadAccountOpt.isPresent()) {
        userInfo.setSalesEmployeeNumber(saleAadAccountOpt.get().getPersonalNumber());
      }
    }

    // Prepare ERP customer details
    Organisation org = getFirstByUserId(currentLoggedUserId);

    Optional<OrganisationCollection> collectionOpt = getCollectionByOrgId(org.getId());

    collectionOpt.ifPresent(collectionSynchronizer(userInfo));

    if (WholesalerUtils.isFinalCustomer(org.getOrgCode()) && !userInfo.isAutonetUserType()) {
      finalCustomerSynchronizer(userInfo).accept(org);
      org = orgService.getByOrgId(org.getParentId())
          .orElseThrow(() -> new IllegalArgumentException("Illegal input argument user id"));
    }

    organisationSynchronizer(userInfo).accept(org);

    clientDetailCacheService.findByClientId(clientId).ifPresent(userInfo::setEshopClient);

    loadCommomUserSetting(userInfo, org);

    loadCustomUserSettings(userInfo);

    loadAlwaysCustomUserSetting(userInfo, loginAffiliate, clientId, saleIdOpt);

    loadCustomUserPermission(userInfo);

    // Save the user info to cache
    userCacheService.put(userInfo);
    return userInfo;
  }

  private void loadCommomUserSetting(UserInfo userInfo, Organisation org) {
    Optional
        .ofNullable(orgService.getByOrgId(org.getParentId())
            .orElseGet(getOrganisationForSale(userInfo, org)))
        .ifPresent(parentOrgToUserInfoSynchronizer(userInfo, org));

    Optional.ofNullable(custSettingsService.findSettingsByOrgId(org.getId()))
        .ifPresent(customerSettingsSynchronizer(userInfo));

    if (userInfo.getFinalCustomerOrgId() != null) {
      Optional.ofNullable(custSettingsService.findSettingsByOrgId(userInfo.getFinalCustomerOrgId()))
      .ifPresent(finalCustomerSettingsSynchronizer(userInfo));
    }

    OwnSettings ownSettings = userInfo.getSettings();
    Optional.ofNullable(userSettingsService.getSettingsByUserId(userInfo.getId()))
        .ifPresent(ownSettings::setUserSettings);

    AxSettingsSynchronizer axSettingsSynchronizer =
        synAxSettings.stream().filter(syncAx -> syncAx.take(userInfo)).findFirst()
            .orElseThrow(() -> new NoSuchElementException("Not support this user role"));
    axSettingsSynchronizer.syncAxSetting(userInfo);

    Optional.ofNullable(userInfo.getDefaultBranchId())
        .ifPresent(customerBranchSynchronizer(userInfo));

    syncEshopGlobalSetting(ownSettings);

    loadOrderLocations(userInfo);

    setBrandPriorityAvailFilterByRole(userInfo);

    setVatTypeDisplay(userInfo);

    setAvailDisplaySettings(userInfo);

    setExternalPartSettings(userInfo);

    // Verifies user accepted all legal terms
    userInfo.setLegalAccepted(
        legalTermService.isAllTermsAccepted(userInfo.getOrganisationId(), userInfo.getLanguage()));
  }

  private void syncEshopGlobalSetting(OwnSettings ownSettings) {
    List<String> globalSettingsToSync = Lists.newArrayList(EshopGlobalSettingEnum.values()).stream()
        .map(EshopGlobalSettingEnum::toValue).collect(Collectors.toList());
    Map<String, EshopGlobalSettingDto> settingByCodes =
        eshopGlobalSettingCacheService.getSettingByCodes(globalSettingsToSync);

    EshopGlobalSettingDto mouseOverFlyoutDelay =
        settingByCodes.get(EshopGlobalSettingEnum.MOUSE_OVER_FLYOUT_DELAY.toValue());
    if (Objects.nonNull(mouseOverFlyoutDelay) && mouseOverFlyoutDelay.isEnabled()) {
      ownSettings.setMouseOverFlyoutDelay(mouseOverFlyoutDelay.getSettingValue());
    }
    Optional<EshopGlobalSettingDto> enhancedUsedPartsReturnProcOpt = Optional.ofNullable(
        settingByCodes.get(EshopGlobalSettingEnum.ENHANCED_USEDPARTS_RETURN_PROC.toValue()));
    enhancedUsedPartsReturnProcOpt.ifPresent(globalSetting -> ownSettings
        .setEnhancedUsedPartsReturnProcEnabled(globalSetting.isEnabled()));
    Optional<EshopGlobalSettingDto> couponModuleOpt =
        Optional.ofNullable(settingByCodes.get(EshopGlobalSettingEnum.COUPON_MODULE.toValue()));
    couponModuleOpt
        .ifPresent(globalSetting -> ownSettings.setCouponModuleEnabled(globalSetting.isEnabled()));
  }

  private void loadOrderLocations(UserInfo userInfo) {
    if (userInfo.isSalesNotOnBehalf() || userInfo.isAdmin() || !userInfo.hasCust()) {
      return;
    }
    if (orderLocationBuilder != null) {
      final List<OrderLocation> orderLocations = orderLocationBuilder.buildOrderLocations(
          userInfo.getCustomer().getGrantedBranches(), userInfo.getCompanyName(),
              null, paymentMethodService.getPaymentMethodOptions(userInfo.isSaleOnBehalf()), null);
      userInfo.setOrderLocations(orderLocations);
    }
  }

  /**
   * Loads custom user settings.
   *
   * @param userInfo the current user login
   */
  protected void loadCustomUserSettings(UserInfo userInfo) {
    // do nothing
  }

  /**
   * Loads always custom user settings.
   *
   * @param userInfo the current user login
   */
  protected void loadAlwaysCustomUserSetting(UserInfo userInfo, String loginAffiliate,
      String clientId, Optional<Long> saleIdOpt) {
    if (!saleIdOpt.isPresent()) {
      return;
    }
    final Long salesIdVal = saleIdOpt.get();
    final UserInfo saleUser = load(salesIdVal, loginAffiliate, clientId, Optional.empty());
    userInfo.setSalesId(salesIdVal);
    userInfo.setSalesUsername(saleUser.getUsername());
    userInfo.setSalesEmployeeNumber(StringUtils.EMPTY);

    // #3387: Connect4Sales CH - When a sales agent selects a customer,
    // the UI language should be always the sales agent's profile language,
    // not the customer's profile language
    // we always set sales's language to on behalf users
    userInfo.setLanguage(saleUser.getLanguage());

    // Get userinfo saleId from front end token, since it is stored inside the token only
    // so we are unable to load it at the time syncUser from Database
    // if this is normal user then user info salesId must be null
    Optional.ofNullable(userInfo.getSettings().getDvseCatalogUri())
        .map(uri -> DvseUriBuilder.builder().uri(uri).saleId(userInfo.getSalesIdString()).build()
            .getUri())
        .ifPresent(uri -> userInfo.getSettings().addExternalUrls(LinkPartnerEnum.DVSE, uri));
  }

  /**
   * Loads custom user permission.
   *
   * @param userInfo the current user login
   */
  protected void loadCustomUserPermission(UserInfo userInfo) {
    // do nothing
  }

  private static Supplier<Organisation> getOrganisationForSale(UserInfo user, Organisation org) {
    return () -> user.isSalesUser() ? org : null;
  }

  private static Consumer<OrganisationCollection> collectionSynchronizer(UserInfo userInfo) {
    return collection -> {
      userInfo.setCollectionId(collection.getId());
      userInfo.setCollectionShortname(collection.getShortname());
      userInfo.setCollectionName(collection.getName());
    };
  }

  private static Consumer<Organisation> organisationSynchronizer(UserInfo userInfo) {
    return org -> {
      userInfo.setOrganisationId(org.getId());
      userInfo.setAffiliateShortName(org.getShortname());
    };
  }

  private static Consumer<Organisation> finalCustomerSynchronizer(UserInfo userInfo) {
    return org -> userInfo
        .setFinalCustomer(OrganisationConverters.organisationConverter().apply(org));
  }

  private Consumer<Organisation> parentOrgToUserInfoSynchronizer(UserInfo userInfo,
      Organisation org) {
    return parentOrg -> {
      final Organisation rightOrg = getRightOrganisation(userInfo, org, parentOrg);
      userInfo.setCompanyName(parentOrg.getName());
      userInfo.setAffiliateShortName(rightOrg.getShortname());
      OwnSettings ownSettings = userInfo.getSettings();
      ownSettings.setAffSettings(
          orgCollectionService.findSettingsByCollectionShortname(userInfo.getCollectionShortname()));
      if (!userInfo.isCustChecked()) {
        return;
      }
      final String custNr = org.getCustomerNrByOrg();
      customerCacheService.getCachedCustomer(custNr, userInfo.getCompanyName())
          .ifPresent(customerInfoSynchronizer(userInfo));
    };
  }

  private void setAvailDisplaySettings(UserInfo userInfo) {
    OwnSettings ownSettings = userInfo.getSettings();
    if (userInfo.isAdmin() || userInfo.isSalesAssistantRole()) {
      return;
    }

    if (userInfo.getSupportedAffiliate().isSupportAvailDisplayAffiliate()) {
      ownSettings.setAvailDisplaySettings(ownSettings.getAvailDisplaySettingsConverted());
    }
  }

  private void setVatTypeDisplay(UserInfo userInfo) {
    OwnSettings ownSettings = userInfo.getSettings();
    if (userInfo.isAdmin() || userInfo.isSalesAssistantRole()) {
      return;
    }
    if (userInfo.getSupportedAffiliate().isPdpAffiliate()) {
      ownSettings.setVatTypeDisplay(ownSettings.getAffVatTypeDisplay());
    }
  }

  private void setBrandPriorityAvailFilterByRole(UserInfo userInfo) {
    OwnSettings ownSettings = userInfo.getSettings();
    if (userInfo.isSaleOnBehalf()) {
      ownSettings.setBrandPriorityAvailFilter(ownSettings.getC4sBrandPriorityAvailFilter());
      return;
    }

    if (userInfo.isCustomerRole() || userInfo.isFinalUserRole()) {
      ownSettings.setBrandPriorityAvailFilter(ownSettings.getCustomerBrandPriorityAvailFilter());
      return;
    }

    ownSettings
        .setBrandPriorityAvailFilter(AffiliateSettingConstants.DEFAULT_BRAND_PRIORITY_AVAIL_FILTER);
  }

  private static Organisation getRightOrganisation(UserInfo userInfo, Organisation org,
      Organisation parentOrg) {
    if (userInfo.isAutonetUser()) {
      return parentOrg;
    }
    if (!userInfo.isCustChecked()) {
      return org;
    }
    return parentOrg;
  }

  private Consumer<Customer> customerInfoSynchronizer(UserInfo userInfo) {
    return customer -> {
      // Update show pfand article configuration per affiliate
      customer.setAllowShowPfandArticle(BooleanUtils.toBoolean(
          supportedAffiliateRepo.isShowPfandArticleByShortName(userInfo.getAffiliateShortName())));
      userInfo.setCustomer(customer);
      userInfo.setAddresses(customerCacheService.getCachedCustomerAddresses(userInfo.getCustNrStr(),
          userInfo.getCompanyName()));
    };
  }

  private Consumer<CustomerSettings> customerSettingsSynchronizer(UserInfo userInfo) {
    return custSettings -> {
      userInfo.getSettings().setSessionTimeoutSeconds(custSettings.getSessionTimeoutSeconds());
      userInfo.getSettings().setViewBilling(custSettings.isViewBilling());
      userInfo.getSettings().setNetPriceConfirm(custSettings.isNetPriceConfirm());
      userInfo.getSettings().setShowOciVat(custSettings.isShowOciVat());
      userInfo.getSettings().setDemoCustomer(custSettings.isDemoCustomer());
      userInfo.getSettings().setNormautoDisplay(custSettings.isNormautoDisplay());
      userInfo.getSettings().setWholeSalerHasNetPrice(custSettings.isWssShowNetPrice());
    };
  }

  private Consumer<CustomerSettings> finalCustomerSettingsSynchronizer(UserInfo userInfo) {
    return custSettings -> {
      userInfo.getSettings().setWssDeliveryProfile(
          custSettings.getWssDeliveryProfile() != null ? WssDeliveryProfileConverters
              .convertToDeliveryProfileDto(custSettings.getWssDeliveryProfile()) : null);
      userInfo.getSettings().setWssMarginGroup(custSettings.getWssMarginGroup());
      userInfo.getSettings().setFinalCustomerHasNetPrice(custSettings.isNetPriceView());
    };
  }
  private Consumer<String> customerBranchSynchronizer(UserInfo userInfo) {
    return defaultBranchId -> {
      if (StringUtils.isBlank(defaultBranchId)) {
        userInfo.setDefaultBranchName(StringUtils.EMPTY);
        return;
      }
      // Connect-Czech #3401
      final SupportedAffiliate affiliate =
          SupportedAffiliate.fromDesc(userInfo.getAffiliateShortName());
      if (affiliate.isCzAffiliate()) {
        Optional.ofNullable(userInfo.getCustomer().getBranch()).ifPresent(
            customerBranch -> userInfo.setDefaultBranchName(customerBranch.getBranchName()));
        return;
      }
      try {
        final Optional<CustomerBranch> defaultBranch =
            customerExtService.searchBranchById(userInfo.getCompanyName(), defaultBranchId);
        defaultBranch.ifPresent(
            customerBranch -> userInfo.setDefaultBranchName(customerBranch.getBranchName()));

      } catch (RestClientException ex) {
        log.error("Ax API external has error: ", ex);
        // Set as default if throw exception
        userInfo.setDefaultBranchName(StringUtils.EMPTY);
      }
    };
  }

  private Organisation getFirstByUserId(long userId) {
    return orgService.getFirstByUserId(userId).orElseThrow(() -> {
      log.error("No organisation found by id = {}", userId);
      return new IllegalArgumentException("Illegal input argument user id");
    });
  }

  private Optional<OrganisationCollection> getCollectionByOrgId(int orgId) {
    Optional<OrganisationCollection> collectionOpt =
        orgCollectionService.getCollectionByOrgId(orgId);
    if (!collectionOpt.isPresent()) {
      throw new IllegalArgumentException(
          String.format("Cannot find collection of orgId = %s", orgId));
    }
    return collectionOpt;
  }

  private void setExternalPartSettings(UserInfo userInfo) {
    OwnSettings ownSettings = userInfo.getSettings();
    if (userInfo.isAdmin() || userInfo.isSalesAssistantRole()) {
      return;
    }
    ownSettings.setExternalPartSettings(ownSettings.getExternalPartSettingsConverted());
  }
}
