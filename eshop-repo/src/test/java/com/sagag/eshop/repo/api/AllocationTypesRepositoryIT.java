package com.sagag.eshop.repo.api;

import com.google.common.collect.Iterables;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.AllocationType;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for {@link AllocationTypeRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class AllocationTypesRepositoryIT {

  @Autowired
  private AllocationTypeRepository allocationTypesRepo;

  @Test
  public void testfindOneByid() {
    log.debug("starting OrgOrderSettingsRepository");
    final Optional<AllocationType> allocationTypes = allocationTypesRepo.findOneById(2);
    Assert.assertThat(true, Is.is(allocationTypes.isPresent()));
    final AllocationType allocationType = allocationTypes.get();
    Assert.assertThat(2, Is.is(allocationType.getId()));
    Assert.assertThat("ALLOCATION_TYPE2", Is.is(allocationType.getDescCode()));
  }

  @Test
  public void testAll() {
    log.debug("starting OrgOrderSettingsRepository");
    final Iterable<AllocationType> allocationTypes = allocationTypesRepo.findAll();
    Assert.assertThat(2, Is.is(Iterables.size(allocationTypes)));
  }

}
