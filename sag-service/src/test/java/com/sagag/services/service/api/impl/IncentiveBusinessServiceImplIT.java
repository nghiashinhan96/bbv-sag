package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.domain.IncentivePointsDto;
import com.sagag.services.incentive.linkgenerator.impl.IncentiveMode;
import com.sagag.services.incentive.response.IncentiveLinkResponse;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.api.IncentiveBusinessService;
import com.sagag.services.service.user.cache.ISyncUserLoader;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * IT for Customer Business.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class IncentiveBusinessServiceImplIT {

  @Autowired
  private IncentiveBusinessService incentiveBusService;

  @Autowired
  private ISyncUserLoader syncUserLoader;

  private UserInfo user;

  @Before
  public void init() {
    user = syncUserLoader.load(27L, StringUtils.EMPTY, StringUtils.EMPTY, Optional.empty());
  }

  @Test
  public void shouldNotGetHappyPointWithDdatCustomer() {
    final Optional<IncentivePointsDto> pointsOpt =
        incentiveBusService.findHappyPointsByCustomer(user);

    Assert.assertThat(pointsOpt.isPresent(), Matchers.is(true));
    Assert.assertThat(pointsOpt.get().getPoints(), Matchers.equalTo(0L));
  }

  @Test
  @Ignore("AX branch is not support for DDCH")
  public void shouldNotGetHappyPointWithDdchCustomer() {
    final Optional<IncentivePointsDto> pointsOpt =
        incentiveBusService.findHappyPointsByCustomer(user);

    Assert.assertThat(pointsOpt.isPresent(), Matchers.is(true));
    Assert.assertThat(pointsOpt.get().getPoints(), Matchers.equalTo(0L));
  }

  @Test
  public void shouldGetHappyBonusLinkWithDdat() throws CookiePrivacyException {
    final IncentiveLinkResponse bonusLink = incentiveBusService.getIncentiveUrl(user);
    Assert.assertThat(bonusLink, Matchers.notNullValue());
  }
  
  @Test
  public void shouldGetHappyPointForDDCH() throws CookiePrivacyException {
    user.setAffiliateShortName("derendinger-ch");
    final IncentiveLinkResponse bonusLink = incentiveBusService.getIncentiveUrl(user);
    Assert.assertThat(bonusLink, Matchers.notNullValue());
    Assert.assertThat(bonusLink.getUrl(),
        Matchers.startsWith("https://derendingerschweiz-shop.connexservice.com/Login"));
    Assert.assertThat(bonusLink.getMode(), Matchers.is(IncentiveMode.CH_HAPPY_POINTS));
  }
}
