package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link WorkingDayRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class WorkingDayRepositoryIT {

  @Autowired
  private WorkingDayRepository workingDayRepository;

  @Test
  public void testFindOneById() {
    final Optional<WorkingDay> workingDay = workingDayRepository.findById(1);
    Assert.assertThat(workingDay.isPresent(), Is.is(true));
    Assert.assertThat(workingDay.get().getId(), Is.is(1));
    Assert.assertThat(workingDay.get().getCode(), Is.is("WORKING_DAY"));
  }

  @Test
  public void testFindAll() {
    final List<WorkingDay> workingDays = workingDayRepository.findAll();
    Assert.assertThat(CollectionUtils.isEmpty(workingDays), Is.is(false));
    Assert.assertThat(workingDays.size(), Is.is(3));
  }
  
  @Test
  public void testFindByCode() {
    final Optional<WorkingDay> workingDay = workingDayRepository.findOneByCode("WORKING_DAY");
    Assert.assertThat(workingDay.isPresent(), Is.is(true));
    Assert.assertThat(workingDay.get().getId(), Is.is(1));
    Assert.assertThat(workingDay.get().getCode(), Is.is("WORKING_DAY"));
  }
}
