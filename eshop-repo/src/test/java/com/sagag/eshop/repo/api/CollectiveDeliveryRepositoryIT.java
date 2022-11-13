package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.CollectiveDelivery;
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
 * Integration test class for {@link CouponConditionsRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class CollectiveDeliveryRepositoryIT {

  @Autowired
  private CollectiveDeliveryRepository collectiveDeliveryRepo;

  @Test
  public void testfindOneByid() {
    log.debug("starting OrgOrderSettingsRepository");
    final Optional<CollectiveDelivery> collective = collectiveDeliveryRepo.findOneById(2);
    Assert.assertThat(true, Is.is(collective.isPresent()));
    Assert.assertThat(2, Is.is(collective.get().getId()));
  }
}
