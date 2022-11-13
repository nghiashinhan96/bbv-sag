package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Integration test for SupportedAffiliateRepository JPA repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class SupportedAffiliateRepositoryIT {

  private static final String MATIK_AT = "matik-at";

  private static final String MATIK_CH = "matik-ch";

  private static final String RBE = "rbe";

  private static final String[] AT_AFFILIATE_COMPANY_NAMES =
      new String[] { "Derendinger-Austria", "Matik-Austria" };

  @Autowired
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Test
  public void testFindByWithinCountryOfShortName() {
    String affiliateShortName = MATIK_AT;
    List<SupportedAffiliate> affiliates =
        supportedAffiliateRepo.findByWithinCountryOfShortName(affiliateShortName);
    Assert.assertThat(affiliates, Matchers.not(Matchers.empty()));
    Assert.assertThat(
        affiliates.stream().map(entity -> entity.getCompanyName()).collect(Collectors.toList()),
        Matchers.contains(AT_AFFILIATE_COMPANY_NAMES));
  }

  @Test
  public void testFindFirstByShortName() {
    String affiliateShortName = MATIK_AT;
    Optional<SupportedAffiliate> affiliateOpt =
        supportedAffiliateRepo.findFirstByShortName(affiliateShortName);
    Assert.assertThat(affiliateOpt.isPresent(), Matchers.is(true));
  }

  @Test
  public void shouldReturnNotShowPfandArticleOfMch() {
    String affiliate = MATIK_CH;
    boolean showPfandArticle = supportedAffiliateRepo.isShowPfandArticleByShortName(affiliate);
    Assert.assertThat(showPfandArticle, Matchers.is(false));
  }

  @Test
  public void shouldReturnShowPfandArticleOfMat() {
    String affiliate = MATIK_AT;
    boolean showPfandArticle = supportedAffiliateRepo.isShowPfandArticleByShortName(affiliate);
    Assert.assertThat(showPfandArticle, Matchers.is(true));
  }

  @Test
  public void shouldReturnNotShowPfandArticleOfEmptyAffiliate() {
    String affiliate = StringUtils.EMPTY;
    Boolean showPfandArticle = supportedAffiliateRepo.isShowPfandArticleByShortName(affiliate);
    Assert.assertThat(showPfandArticle, Matchers.equalTo(null));
  }

  @Test
  public void testFindCountryShortNameByAffShortName() {

    String result = supportedAffiliateRepo.findCountryShortNameByAffShortName(MATIK_AT);
    Assert.assertThat(result, Matchers.is("Austria"));

    result = supportedAffiliateRepo.findCountryShortNameByAffShortName(MATIK_CH);
    Assert.assertThat(result, Matchers.is("Switzerland"));

    result = supportedAffiliateRepo.findCountryShortNameByAffShortName(RBE);
    Assert.assertThat(result, Matchers.is("Belgium"));
  }

  @Test
  public void testFindByCountryShortCodeWithAT() {
    testAndAssertFindByCountryShortCode("at");
  }

  @Test
  public void testFindByCountryShortCodeWithCH() {
    testAndAssertFindByCountryShortCode("ch");
  }

  private void testAndAssertFindByCountryShortCode(String shortCode) {
    List<String> affiliates = supportedAffiliateRepo.findCompanyNameByCountryShortCode(shortCode);
    for (String affiliate : affiliates) {
      log.debug("Affiliate Name = {}", affiliate);
    }
  }

  @Test
  public void findShortNameByCompanyName_shouldReturnShortName_givenCompanyName() throws Exception {
    String result = supportedAffiliateRepo.findShortNameByCompanyName("Derendinger-Austria").get();
    Assert.assertThat(result, Matchers.is("derendinger-at"));
  }
}
