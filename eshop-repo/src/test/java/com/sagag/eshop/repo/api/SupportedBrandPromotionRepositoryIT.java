package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.enums.ArticleShopType;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Integration test class for {@link SupportedBrandPromotionRepository}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class SupportedBrandPromotionRepositoryIT {

  @Autowired
  private SupportedBrandPromotionRepository repository;

  @Test
  public void givenPromotionTyresAndDDCH_shouldReturnGreaterOrEqualsThanOne() {
    final ArticleShopType type = ArticleShopType.TYRES;
    final String affiliate = SupportedAffiliate.DERENDINGER_CH.getAffiliate();

    // requestDate = 2018.08.01
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2018);
    cal.set(Calendar.MONTH, Calendar.AUGUST);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    final Date requestDate = cal.getTime();

    final List<String> brands = repository.findPromotionBrands(type, affiliate, requestDate);

    Assert.assertThat(brands.size(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void givenDayOfEndDate_shouldReturnGreaterOrEqualsThanOne() {
    final ArticleShopType type = ArticleShopType.TYRES;
    final String affiliate = SupportedAffiliate.DERENDINGER_CH.getAffiliate();

    // requestDate = 2018.11.01 00:00:00.000
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2018);
    cal.set(Calendar.MONTH, Calendar.NOVEMBER);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    final Date requestDate = cal.getTime();

    final List<String> brands = repository.findPromotionBrands(type, affiliate, requestDate);

    Assert.assertThat(brands.size(), Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void givenWrongPromotionDate_shouldReturnEmpty() {
    final ArticleShopType type = ArticleShopType.TYRES;
    final String affiliate = SupportedAffiliate.DERENDINGER_CH.getAffiliate();

    // requestDate = 2018.07.31
    final Date requestDate = DateTime.parse("2018-07-31").toDate();

    final List<String> brands = repository.findPromotionBrands(type, affiliate, requestDate);

    Assert.assertThat(brands.size(), Matchers.equalTo(0));
  }

}
