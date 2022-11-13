package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.BranchOpeningTime;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.WeekDay;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class BranchOpeningTimeRepositoryIT {

  private static final int BRANCH_ID = 13;

  private static final int BRANCH_NR = 1013;

  @Autowired
  private BranchOpeningTimeRepository repository;

  @Test
  public void givenBranchNrShouldReturnOpeningTimeList() {
    List<BranchOpeningTime> botList = repository.findByBranchNr(BRANCH_NR);
    assertBranchOpeningTime(botList);
  }

  @Test
  public void givenBranchIdShouldReturnOpeningTimeList() {
    List<BranchOpeningTime> botList = repository.findByBranchId(BRANCH_ID);
    assertBranchOpeningTime(botList);
  }

  @Test
  public void givenBranchIdShouldRemoveOpeningTimeList() {
    repository.deleteAllByBranchId(BRANCH_ID);
  }

  @Test
  public void givenBranchNumberListShouldReturnOpeningTimeList() {
    List<BranchOpeningTime> botList = repository.findByBranchNrList(Arrays.asList(BRANCH_NR));
    assertBranchOpeningTime(botList);
  }

  private void assertBranchOpeningTime(List<BranchOpeningTime> botList) {
    assertThat(botList.isEmpty(), Matchers.is(false));

    for (BranchOpeningTime bot : botList) {
      assertThat(bot.getBranchId(), Matchers.is(BRANCH_ID));
      assertThat(bot.getWeekDay(), Matchers.isIn(WeekDay.values()));
      assertThat(bot.getOpeningTime(), Matchers.is(Time.valueOf(LocalTime.of(7, 30))));
      assertThat(bot.getClosingTime(), Matchers.is(Time.valueOf(LocalTime.of(17, 30))));
      assertThat(bot.getLunchStartTime(), Matchers.nullValue());
      assertThat(bot.getLunchEndTime(), Matchers.nullValue());
    }
  }
}
