package com.sagag.services.ax.availability;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.availability.filter.AxAvailabilityFilter;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

/**
 * IT to verify {@link AxAvailabilityFilter}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
public class AxAvailabilityFilterIT {

  private static final String COUNTRY_NAME = AxDataTestUtils.COUNTRY_NAME_AT;

  @Autowired
  private AxAvailabilityFilter filter;

  @Test
  public void shouldReturnFullAvailabilitiesWithNormalCase() {
    System.setProperty("user.timezone", "CET");
    final ArticleSearchCriteria criteria = AxDataTestUtils.buildTourArticleSearchCriteria(
        AxDataTestUtils.buildNextWorkingDates("2018-03-30T00:00:00"));

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilitiesWithTour());
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        Collections.emptyList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

  @Test
  public void shouldReturnFullAvailabilitiesAfterHolidays() {
    System.setProperty("user.timezone", "CET");
    final ArticleSearchCriteria criteria = AxDataTestUtils.buildTourArticleSearchCriteria(
        AxDataTestUtils.buildNextWorkingDates("2018-04-03T00:00:00"));

    final ArticleDocDto article = new ArticleDocDto();
    article.setAvailabilities(AxDataTestUtils.availabilitiesWithTour());
    article.setPrice(AxDataTestUtils.articlePrice());

    final List<Availability> availabilities = filter.filterAvailabilities(article, criteria,
        Collections.emptyList(), AxDataTestUtils.mockWorkingHours(), COUNTRY_NAME);

    AxDataTestUtils.logObjects(availabilities);
    Assert.assertNotNull(availabilities);
  }

}
