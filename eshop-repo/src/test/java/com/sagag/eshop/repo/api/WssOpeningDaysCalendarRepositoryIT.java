package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.WssOpeningDaysCalendar;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for {@link WssOpeningDaysCalendarRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class WssOpeningDaysCalendarRepositoryIT {

  private static final int SPECIAL_ID = 8; // Valid with Integration Testing DB.

  @Autowired
  private WssOpeningDaysCalendarRepository wssOpeningDaysCalendarRepo;

  @Test
  public void testFindOneById() {
    final Optional<WssOpeningDaysCalendar> openingDay =
      wssOpeningDaysCalendarRepo.findById(SPECIAL_ID);
    if (!openingDay.isPresent()) {
      Assert.assertThat(openingDay.isPresent(), Is.is(false));
      return;
    }
    Assert.assertThat(openingDay.isPresent(), Is.is(true));
    final WssOpeningDaysCalendar openingDaysCalendar = openingDay.get();
    Assert.assertThat(openingDaysCalendar.getId(), Is.is(SPECIAL_ID));
    Assert.assertThat(openingDaysCalendar.getCountry().getId(), Matchers.notNullValue());
    Assert.assertThat(StringUtils.isBlank(openingDaysCalendar.getExceptions()), Is.is(false));
  }

}
