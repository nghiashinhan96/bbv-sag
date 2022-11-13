package com.sagag.services.service.user.cache.impl;

import com.google.common.primitives.Bytes;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.enums.LinkPartnerEnum;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.country.UserLoaderMode;
import com.sagag.services.common.utils.HashUtils;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.domain.sag.external.ExternalUserSession;
import com.sagag.services.hazelcast.globalsetting.GtmotiveGlobalSettingPredicates;
import com.sagag.services.mdm.api.DvseUserService;
import com.sagag.services.service.profiles.UserLoaderModeProfile;
import com.sagag.services.service.user.permission.DefaultPermissionBuilder;
import com.sagag.services.thule.api.ThuleService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
@UserLoaderModeProfile(UserLoaderMode.ESHOP)
@Slf4j
public class EshopSyncUserLoaderImpl extends AbstractSyncUserLoader {

  private static final int SALT_MAX_LENGTH = 8;

  private static final String STG_INFO_URL_PATTERN =
      "https://www.sag.cz/saginfo/?customer=%s";

  private static final String LAXIMO_URL_PATTERN =
      "https://vinsearch.sag.cz/TreeColumnsCatalogs.aspx?username=%s&hash=%s";

  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  @Autowired
  private DvseUserService dvseUserService;

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private GtmotiveGlobalSettingPredicates gtmotiveGlobalSettingPredicates;

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private ThuleService thuleService;

  @Autowired
  private UserService userService;

  @Override
  protected void loadCustomUserSettings(UserInfo userInfo) {

    if (cisSupportedPredicate().test(userInfo)) {
      final ExternalUserSession session = new ExternalUserSession();
      session.setCustomerId(userInfo.getCustNrStr());
      session.setLId(userInfo.getLanguage());
      session.setSid(UUID.randomUUID().toString());

      Optional.ofNullable(STG_INFO_URL_PATTERN)
      .map(url -> String.format(url, userInfo.getCustNr()))
      .filter(StringUtils::isNotBlank)
      .ifPresent(url -> externalUrlsAppender(userInfo).accept(LinkPartnerEnum.STG_INFO, url));

      final String hash = buildHash(userInfo.getCustNr());
      Optional.ofNullable(LAXIMO_URL_PATTERN)
          .map(url -> String.format(url, userInfo.getCustNr(), hash))
          .filter(StringUtils::isNotBlank)
          .ifPresent(url -> externalUrlsAppender(userInfo).accept(LinkPartnerEnum.LAXIMO, url));

      userInfo.setExternalUserSession(session);
    }

    final List<ExternalUserDto> externalUsers =
        externalUserService.searchExternalUsersByUserId(userInfo.getId());
    if (CollectionUtils.isEmpty(externalUsers)) {
      log.warn("Not found any external users of user id = {}", userInfo.getId());
      return;
    }

    // Get more info with AT customers
    if (dvseSupportedPredicate().test(userInfo)) {
      externalUsers.stream()
          .filter(extUser -> extUser.getExternalApp() == ExternalApp.DVSE && extUser.isActive())
          .findFirst().ifPresent(externalUserSynchronizer(userInfo));
    }
  }

  private String buildHash(String customerNr) {
    StringBuilder tobeMD5String = new StringBuilder();
    String salt = RandomStringUtils.randomAlphanumeric(SALT_MAX_LENGTH).toLowerCase();
    tobeMD5String.append(customerNr).append(salt);
    String encodedBase64 = Base64Utils.encodeToString(
        Bytes.concat(HashUtils.hashMD5ReturnRawBinary(tobeMD5String.toString()), salt.getBytes()));
    try {
      return URLEncoder.encode(encodedBase64, UTF_8);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException("Url encode failing for generating the hash");
    }
  }

  private Predicate<UserInfo> dvseSupportedPredicate() {
    return userInfo -> {
      if (!userInfo.isAffChecked()) {
        return false;
      }
      return userService.hasPermission(userInfo.getId(), PermissionEnum.DVSE);
    };
  }

  private static Predicate<UserInfo> cisSupportedPredicate() {
    return userInfo -> {
      if (!userInfo.isAffChecked()) {
        return false;
      }
      SupportedAffiliate affiliate = userInfo.getSupportedAffiliate();
      return affiliate != null && (affiliate.isCzAffiliate() || affiliate.isSagCzAffiliate());
    };
  }

  private Consumer<ExternalUserDto> externalUserSynchronizer(UserInfo userInfo) {
    return user -> {
      userInfo.getSettings().setDvseUserActive(user.isActive());
      // Default ESID is empty string which is normal user
      Optional
          .ofNullable(dvseUserService.getDvseCatalogUri(userInfo.getSupportedAffiliate(),
              user.getUsername(), user.getPassword(), StringUtils.EMPTY))
          .filter(StringUtils::isNotBlank)
          .ifPresent(url -> externalUrlsAppender(userInfo).accept(LinkPartnerEnum.DVSE, url));
    };
  }

  private static BiConsumer<LinkPartnerEnum, String> externalUrlsAppender(UserInfo userInfo) {
    return (linkParter, url) -> userInfo.getSettings().addExternalUrls(linkParter, url);
  }

  private static String defaultLoginAffiliate(String loginAffiliate, String userAffiliate) {
    if (StringUtils.isBlank(loginAffiliate)
        || StringUtils.equalsIgnoreCase(loginAffiliate, userAffiliate)) {
      return userAffiliate;
    }
    return StringUtils.defaultString(loginAffiliate);
  }

  @Override
  protected void loadAlwaysCustomUserSetting(UserInfo userInfo, String loginAffiliate,
      String clientId, Optional<Long> saleIdOpt) {
    super.loadAlwaysCustomUserSetting(userInfo, loginAffiliate, clientId, saleIdOpt);

    final String currentLoginAffiliate =
        defaultLoginAffiliate(loginAffiliate, userInfo.getAffiliateShortName());

    final boolean isSalesMode = userInfo.isSaleOnBehalf() || userInfo.isSalesUser();
    final Optional<String> thuleUrlOpt = thuleService.findThuleDealerUrlByAffiliate(
        currentLoginAffiliate, isSalesMode, userInfo.getUserLocale());
    userInfo.getSettings().removeThuleDealerUrl();
    thuleUrlOpt.ifPresent(url -> externalUrlsAppender(userInfo).accept(LinkPartnerEnum.THULE, url));

  }

  @Override
  protected void loadCustomUserPermission(UserInfo userInfo) {
    userInfo.getPermissions().addAll(DefaultPermissionBuilder.buildDefaultPermissions(userInfo));
    DefaultPermissionBuilder.addVinPackagesFunctionToUserInfo(userInfo);
    log.debug("SalesId {}", userInfo.getSalesId());

    final PermissionEnum vinPermission = PermissionEnum.VIN;
    final Predicate<UserInfo> vinPermissionOfUserInfoPredicate =
        userSession -> !userSession.hasPermissionByName(vinPermission);
    final Predicate<UserInfo> userOnbehalfAdminUserTypePredicate = UserInfo::isSaleOnBehalf;
    final Predicate<UserInfo> allowUsedVinOfC4SPredicate =
        gtmotiveGlobalSettingPredicates.andPredicates(
            Arrays.asList(userOnbehalfAdminUserTypePredicate, vinPermissionOfUserInfoPredicate));
    if (allowUsedVinOfC4SPredicate.test(userInfo)) {
      permissionService.findByPermission(vinPermission).ifPresent(userInfo.getPermissions()::add);
    }
  }
}
