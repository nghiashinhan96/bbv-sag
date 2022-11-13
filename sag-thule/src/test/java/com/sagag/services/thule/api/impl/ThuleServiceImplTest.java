package com.sagag.services.thule.api.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.thule.DataProvider;
import com.sagag.services.thule.domain.BuyersGuideData;
import com.sagag.services.thule.domain.ThuleProperties;
import com.sagag.services.thule.extractor.BuyersGuideDataExtractor;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ThuleServiceImplTest {

  private static final String DF_COUNTRY = "at";

  private static final String DF_URL =
      "https://www.thule.com/de-at/buyers-guide?dealerId=test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB";

  @InjectMocks
  private ThuleServiceImpl service;

  @Mock
  private BuyersGuideDataExtractor buyersGuideDataExtractor;

  @Mock
  private ThuleProperties thuleProps;

  @Mock
  private LocaleContextHelper localeContextHelper;

  @Test
  public void givenDDATShouldReturnUrl() {
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    final Locale locale = new Locale("en", DF_COUNTRY);
    final String expUrl =
        "https://www.thule.com/en-at/buyers-guide?dealerId=test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB";

    Mockito.when(localeContextHelper
      .findCountryByAffiliate(SupportedAffiliate.fromDescSafely(affiliate)))
      .thenReturn(locale.getCountry());
    Mockito.when(localeContextHelper.defaultLocale(locale)).thenReturn(locale);

    testAndAssertFindThuleDealerUrl(affiliate, locale, Matchers.is(true), Matchers.equalTo(expUrl));

    Mockito.verify(thuleProps, Mockito.times(1)).getDealerIdMap();
    Mockito.verify(thuleProps, Mockito.times(1)).getEndpoint();
  }

  @Test
  public void givenDDATWithLanguageLocaleShouldReturnUrl() {
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    final Locale locale = new Locale("as", DF_COUNTRY);
    final String expUrl =
        "https://www.thule.com/as-at/buyers-guide?dealerId=test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB";


    Mockito.when(localeContextHelper
      .findCountryByAffiliate(SupportedAffiliate.fromDescSafely(affiliate)))
      .thenReturn(locale.getCountry());
    Mockito.when(localeContextHelper.defaultLocale(locale)).thenReturn(locale);

    testAndAssertFindThuleDealerUrl(affiliate, locale, Matchers.is(true), Matchers.equalTo(expUrl));

    Mockito.verify(thuleProps, Mockito.times(1)).getDealerIdMap();
    Mockito.verify(thuleProps, Mockito.times(1)).getEndpoint();
  }

  @Test
  public void givenDDATWithNullLocaleShouldReturnUrl() {
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    final Locale locale = null;
    final String expUrl = DF_URL;

    Mockito.when(localeContextHelper
      .findCountryByAffiliate(SupportedAffiliate.fromDescSafely(affiliate)))
      .thenReturn(DF_COUNTRY);
    Mockito.when(localeContextHelper.defaultLocale(locale)).thenReturn(new Locale("de", DF_COUNTRY));

    testAndAssertFindThuleDealerUrl(affiliate, locale, Matchers.is(true), Matchers.equalTo(expUrl));

    Mockito.verify(thuleProps, Mockito.times(1)).getDealerIdMap();
    Mockito.verify(thuleProps, Mockito.times(1)).getEndpoint();
  }

  @Test
  public void givenEmptyAffShouldReturnEmpty() {
    final String affiliate = StringUtils.EMPTY;
    final Locale locale = null;
    final String expUrl = StringUtils.EMPTY;
    testAndAssertFindThuleDealerUrl(affiliate, locale, Matchers.is(false), Matchers.equalTo(expUrl));

    Mockito.verify(thuleProps, Mockito.times(0)).getDealerIdMap();
    Mockito.verify(thuleProps, Mockito.times(0)).getEndpoint();
  }

  @Test
  public void givenUnsupportedAffShouldReturnEmpty() {
    final String affiliate =  SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    final Locale locale = null;
    final String expUrl = StringUtils.EMPTY;
    Mockito.when(thuleProps.getDealerIdMap()).thenReturn(DataProvider.getDealerIdMap());
    Mockito.when(thuleProps.getEndpoint()).thenReturn(DataProvider.THULE_ENDPOINT);
    Mockito.when(thuleProps.getDealerIdMap().get(affiliate)).thenReturn(null);

    final Optional<String> optional =
        service.findThuleDealerUrlByAffiliate(affiliate, false, locale);

    Assert.assertThat(optional.isPresent(), Matchers.is(false));
    Assert.assertThat(optional.orElse(StringUtils.EMPTY), Matchers.equalTo(expUrl));

    Mockito.verify(thuleProps, Mockito.times(1)).getDealerIdMap();
    Mockito.verify(thuleProps, Mockito.times(0)).getEndpoint();
  }

  private void testAndAssertFindThuleDealerUrl(String aff, Locale locale,
      Matcher<Boolean> optionalMatcher, Matcher<String> urlMatcher) {
    Mockito.when(thuleProps.getDealerIdMap()).thenReturn(DataProvider.getDealerIdMap());
    Mockito.when(thuleProps.getEndpoint()).thenReturn(DataProvider.THULE_ENDPOINT);

    final Optional<String> optional =
        service.findThuleDealerUrlByAffiliate(aff, false, locale);

    Assert.assertThat(optional.isPresent(), optionalMatcher);
    Assert.assertThat(optional.orElse(StringUtils.EMPTY), urlMatcher);
  }

  @Test
  public void givenSampleMapShouldAddBuyersGuide() {
    final Map<String, String> formData = DataProvider.buildSampleMap();
    Mockito.doCallRealMethod().when(buyersGuideDataExtractor).apply(formData);
    Optional<BuyersGuideData> optional = service.addBuyersGuide(formData);
    Assert.assertThat(optional.isPresent(), Matchers.is(true));
  }

  @Test
  public void givenEmptyMapShouldAddBuyersGuide() {
    final Map<String, String> formData = Collections.emptyMap();
    Mockito.doCallRealMethod().when(buyersGuideDataExtractor).apply(formData);
    Optional<BuyersGuideData> optional = service.addBuyersGuide(formData);
    Assert.assertThat(optional.isPresent(), Matchers.is(false));
  }
}
