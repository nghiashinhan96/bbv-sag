package com.sagag.services.thule.api.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.thule.DataProvider;
import com.sagag.services.thule.ThuleApplication;
import com.sagag.services.thule.api.ThuleService;
import com.sagag.services.thule.domain.BuyersGuideData;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ThuleApplication.class })
@ChEshopIntegrationTest
@Slf4j
public class ThuleServiceImplIT {

  @Autowired
  private ThuleService service;

  @Test
  public void givenDDCHShouldReturnUrl() {
    final String affiliate = SupportedAffiliate.DERENDINGER_CH.getAffiliate();
    final Locale locale = Locale.ENGLISH;
    final String expUrl =
        "https://www.thule.com/en-ch/buyers-guide?dealerId=5500089A-78BB-4F72-BE54-C08506255AB8";
    testAndAssertFindThuleDealerUrl(affiliate, locale, false, Matchers.is(true), Matchers.equalTo(expUrl));
  }

  @Test
  public void givenAffiliatesShouldReturnSafely() {
    Locale[] supportedLocales = new Locale[] {
        Locale.ENGLISH, Locale.GERMAN, Locale.FRENCH, Locale.ITALIAN
    };

    Boolean[] salesOnbehalfModes = new Boolean[] { true, false };
    final List<String> affiliates = Stream.of(SupportedAffiliate.values())
        .map(SupportedAffiliate::getAffiliate).collect(Collectors.toList());
    affiliates.add(WholesalerUtils.EH_AT);
    affiliates.add(WholesalerUtils.EH_CH);

    for (String affiliate : affiliates) {
      for (Locale locale : supportedLocales) {
        for (boolean salesOnfbehalf : salesOnbehalfModes) {
          testAndAssertFindThuleDealerUrl(
              affiliate, locale, salesOnfbehalf, null, null);
        }
      }
    }
  }

  private void testAndAssertFindThuleDealerUrl(String aff, Locale locale, boolean saleOnbehalfUser,
      Matcher<Boolean> optionalMatcher, Matcher<String> urlMatcher) {
    final Optional<String> optional =
        service.findThuleDealerUrlByAffiliate(aff, saleOnbehalfUser, locale);
    log.info("affiliate = {} - language = {} - C4S = {} - value = {}", aff,
        locale.getLanguage(), saleOnbehalfUser, optional.orElse(StringUtils.EMPTY));

    if (optionalMatcher != null) {
      Assert.assertThat(optional.isPresent(), optionalMatcher);
    }
    if (urlMatcher != null) {
      Assert.assertThat(optional.orElse(StringUtils.EMPTY), urlMatcher);
    }
  }

  @Test
  public void givenFormDataShouldReturnBuyersGuideData() {
    testAndAssertAadBuyersGuide(DataProvider.buildSampleMap(), Matchers.is(true));
  }

  private void testAndAssertAadBuyersGuide(Map<String, String> formData,
      Matcher<Boolean> optionalMatcher) {
    final Optional<BuyersGuideData> optional = service.addBuyersGuide(formData);
    Assert.assertThat(optional.isPresent(), optionalMatcher);
  }
}
