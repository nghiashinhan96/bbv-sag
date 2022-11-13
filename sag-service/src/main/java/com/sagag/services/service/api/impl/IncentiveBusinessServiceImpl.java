package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.domain.IncentivePointsDto;
import com.sagag.services.incentive.domain.IncentivePointsDto.IncentivePointsDtoBuilder;
import com.sagag.services.incentive.linkgenerator.impl.IncentiveMode;
import com.sagag.services.incentive.response.IncentiveLinkResponse;
import com.sagag.services.service.api.IncentiveBusinessService;
import com.sagag.services.service.incentive.CompositeIncentivePointLinkGenerator;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Implementation for incentive points services.
 */
@Service
@IncentiveProfile
public class IncentiveBusinessServiceImpl implements IncentiveBusinessService {

  private static final String USER_ID_MUST_NOT_NULL_MSG = "The given user id must not be null";

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Autowired
  private UserSettingsService userSettingService;

  @Autowired
  private CompositeIncentivePointLinkGenerator compositeIncentivePointLinkGenerator;

  @Override
  public IncentiveLinkResponse getIncentiveUrl(UserInfo user) throws CookiePrivacyException {
    Assert.notNull(user, "The given user must not be null");
    IncentiveLinkResponse response = compositeIncentivePointLinkGenerator.generate(user);

    if (response.getMode() == IncentiveMode.MILES) {
      response.setDescription(
          messageSource.getMessage("miles.description", null, user.getUserLocale()));
    }

    final SupportedAffiliate affiliate = user.getSupportedAffiliate();
    if (!isShowHappyPointByAff().test(affiliate)) {
      return response;
    }
    findHappyPointsByCustomer(user).ifPresent(pointInfo -> {
      response.setPoints(pointInfo.getPoints());
      response.setShowHappyPoints(pointInfo.isShowHappyPoints());
      response.setAcceptHappyPointTerm(pointInfo.isAcceptHappyPointTerm());
    });
    return response;
  }

  @Override
  public Optional<IncentivePointsDto> findHappyPointsByCustomer(final UserInfo user) {

    final SupportedAffiliate affiliate = user.getSupportedAffiliate();
    final IncentivePointsDtoBuilder incentivePointsBuilder = IncentivePointsDto.builder();

    // With affiliate is not DDCH, skip get happy points services.
    incentivePointsBuilder.showHappyPoints(
        user.isShowHappyPoints() && isShowHappyPointByAff().test(affiliate));
    incentivePointsBuilder.acceptHappyPointTerm(user.isAcceptHappyPointTerm());
    final IncentivePointsDto incentivePoint = incentivePointsBuilder.build();
    if (!incentivePoint.isShowHappyPoints()) {
      incentivePoint.setPoints(NumberUtils.LONG_ZERO);
    }
    return Optional.of(incentivePoint);
  }

  private static Predicate<SupportedAffiliate> isShowHappyPointByAff() {
    return affiliate -> affiliate != null && (affiliate.isDch() || affiliate.isTnm());
  }

  @Override
  public void saveAccetpHappyPointTerm(Long userId) {
    Assert.notNull(userId, USER_ID_MUST_NOT_NULL_MSG);

    final VUserDetail userDetail = vUserDetailRepo.findByUserId(userId)
        .orElseThrow(notFoundUser(userId));

    UserSettings userSettings = findUserSettingsByUserLogin(userDetail.getUserSettingId());
    userSettings.setAcceptHappyPointTerm(true);
    userSettingService.updateUserSettings(userSettings);
  }

  private UserSettings findUserSettingsByUserLogin(final Integer userSettingId) {
    Assert.notNull(userSettingId, "The given user setting id must not be null");
    return userSettingService.findUserSettingsById(userSettingId)
        .orElseThrow(notFoundAnyUserSettings());
  }

  private static Supplier<NoSuchElementException> notFoundAnyUserSettings() {
    return () -> new NoSuchElementException("Not found any user settings for this user");
  }

  private static Supplier<NoSuchElementException> notFoundUser(Long userId) {
    return () -> new NoSuchElementException(
        String.format("Not found user info by user id = %s", userId));
  }
}
