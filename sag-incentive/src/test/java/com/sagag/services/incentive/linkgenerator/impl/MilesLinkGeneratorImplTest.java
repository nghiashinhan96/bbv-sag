package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.DataProvider;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Supplier;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class MilesLinkGeneratorImplTest {

  @InjectMocks
  private MilesLinkGeneratorImpl generator;

  @Mock
  private IncentiveProperties incentiveProps;

  @Mock
  private IncentivePointProperties props;

  @Before
  public void setup() {
    Mockito.when(incentiveProps.getMilesPoints()).thenReturn(props);
    Mockito.when(props.getUrl())
    .thenReturn("https://shop.connexservice.com/login.php?user=%s&shop=45&hash=%s");
    Mockito.when(props.getHashEncrypt()).thenReturn("MD5");
    Mockito.when(props.getSecureKey()).thenReturn("A34CD799D234DA83988440B323B90C55");
    Mockito.when(props.getShopValue()).thenReturn("45");
  }

  @Test
  public void shouldGenerateMilesLinkSucceed() throws CookiePrivacyException {
    final SupportedAffiliate affiliate = SupportedAffiliate.TECHNOMAG;
    final boolean showHappyPoints = true;
    final String custNr = "1100005";
    testMilesSupport(affiliate, showHappyPoints, IncentiveMode.MILES);
    testMilesLinkGenerator(custNr, "bharani", "FdcFONWLNYYKY", HashType.BLCK_VAR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldBuildLoginDtoFailed() {
    Supplier<?>[] suppliers = new Supplier<?>[] {};
    generator.buildLogin(suppliers);
  }

  @Test
  public void shouldGenerateMilesLinkSucceedWithOneSupplier() throws CookiePrivacyException {
    final SupportedAffiliate affiliate = SupportedAffiliate.TECHNOMAG;
    final boolean showHappyPoints = true;
    final String custNr = "1100005";
    testMilesSupport(affiliate, showHappyPoints, IncentiveMode.MILES);
    IncentiveLoginDto login = generator.buildLogin(DataProvider.customerNr(custNr));

    final String link = generator.generate(login);
    log.debug("Miles Link = {}", link);
    Assert.assertThat(link, Matchers.startsWith("https://shop.connexservice.com/login.php?user="));
  }

  @Test
  public void shouldGenerateMilesNotSupportedAffiliate() {
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final boolean showHappyPoints = true;
    testMilesSupport(affiliate, showHappyPoints, null);
  }

  @Test
  public void shouldGenerateMilesNullSupportedAffiliate() {
    final SupportedAffiliate affiliate = null;
    final boolean showHappyPoints = true;
    testMilesSupport(affiliate, showHappyPoints, null);
  }

  @Test
  public void shouldGenerateMilesFalseShowHappyPointsSettings() {
    final SupportedAffiliate affiliate = null;
    final boolean showHappyPoints = false;
    testMilesSupport(affiliate, showHappyPoints, null);
  }

  private void testMilesSupport(SupportedAffiliate affiliate, boolean showHappyPoints,
      IncentiveMode expectedResult) {
    final IncentiveMode mode = generator.support(affiliate, showHappyPoints);
    Assert.assertThat(mode, Matchers.is(expectedResult));
  }

  private void testMilesLinkGenerator(String custNr, String username, String password,
      HashType hashType)
      throws CookiePrivacyException {

    final IncentiveLoginDto login = generator.buildLogin(
        DataProvider.customerNr(custNr),
        DataProvider.username(username),
        DataProvider.passwordHash(password, hashType));

    Assert.assertThat(login.getCustNr(), Matchers.is(custNr));
    Assert.assertThat(login.getUserName(), Matchers.nullValue());
    Assert.assertThat(login.getPassword(), Matchers.nullValue());
    Assert.assertThat(login.getType(), Matchers.nullValue());

    final String link = generator.generate(login);
    log.debug("Miles Link = {}", link);

    Assert.assertThat(link, Matchers.startsWith("https://shop.connexservice.com/login.php?user="));
  }

}
