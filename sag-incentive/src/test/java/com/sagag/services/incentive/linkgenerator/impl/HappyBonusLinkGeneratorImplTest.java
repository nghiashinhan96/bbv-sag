package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.DataProvider;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class HappyBonusLinkGeneratorImplTest {

  @InjectMocks
  private HappyBonusLinkGeneratorImpl generator;

  @Mock
  private IncentiveProperties incentiveProps;

  @Mock
  private IncentivePointProperties props;

  @Before
  public void setup() {
    Mockito.when(incentiveProps.getHappyBonus()).thenReturn(props);
    Mockito.when(props.getUrl()).thenReturn("http://dev.happybonus.ch?token=%s");
    Mockito.when(props.getAccessPointKey()).thenReturn("dch");
    Mockito.when(props.getSessionTimeoutMs()).thenReturn(3600000l);
  }

  @Test
  public void shouldGenerateHappyBonusLinkSucceed() throws CookiePrivacyException {
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_CH;
    final boolean showHappyPoints = true;
    final String custNr = StringUtils.EMPTY;
    testHappyBonusSupport(affiliate, showHappyPoints, IncentiveMode.HAPPY_BONUS);
    testHappyBonusLinkGenerator(custNr, "bharani", "FdcFONWLNYYKY", HashType.BLCK_VAR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldBuildLoginDtoFailed() {
    final HashType hashType = HashType.BLCK_VAR;
    generator.buildLogin(DataProvider.username("bharani"),
        DataProvider.passwordHash("FdcFONWLNYYKY1", hashType));
  }

  @Test
  public void shouldGenerateHappyBonusNotSupportedAffiliate() {
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final boolean showHappyPoints = true;
    testHappyBonusSupport(affiliate, showHappyPoints, null);
  }

  @Test
  public void shouldGenerateHappyBonusNullSupportedAffiliate() {
    final SupportedAffiliate affiliate = null;
    final boolean showHappyPoints = true;
    testHappyBonusSupport(affiliate, showHappyPoints, null);
  }

  @Test
  public void shouldGenerateHappyBonusFalseShowHappyPointsSettings() {
    final SupportedAffiliate affiliate = null;
    final boolean showHappyPoints = false;
    testHappyBonusSupport(affiliate, showHappyPoints, null);
  }

  private void testHappyBonusSupport(SupportedAffiliate affiliate, boolean showHappyPoints,
      IncentiveMode expectedResult) {
    final IncentiveMode mode = generator.support(affiliate, showHappyPoints);
    Assert.assertThat(mode, Matchers.is(expectedResult));
  }

  private void testHappyBonusLinkGenerator(String custNr, String username, String password,
      HashType hashType)
      throws CookiePrivacyException {

    final IncentiveLoginDto login = generator.buildLogin(
        DataProvider.customerNr(custNr),
        DataProvider.username(username),
        DataProvider.passwordHash(password, hashType));

    Assert.assertThat(login.getCustNr(), Matchers.nullValue());
    Assert.assertThat(login.getUserName(), Matchers.is(username));
    Assert.assertThat(login.getPassword(), Matchers.is(password));
    Assert.assertThat(login.getType(), Matchers.is(hashType));

    final String link = generator.generate(login);
    log.debug("HappyBonus Link = {}", link);

    Assert.assertThat(link, Matchers.startsWith("http://dev.happybonus.ch?token="));
  }

}
