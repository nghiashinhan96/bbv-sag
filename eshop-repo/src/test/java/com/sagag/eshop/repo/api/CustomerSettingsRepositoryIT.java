package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
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
 * Integration test class for {@link CustomerSettingsRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class CustomerSettingsRepositoryIT {

  @Autowired
  private CustomerSettingsRepository orgOrderSettingsRepo;

  @Test
  public void testfindOneByid() {
    log.debug("starting OrgOrderSettingsRepository");
    final Optional<CustomerSettings> orgOrderSettings =
        orgOrderSettingsRepo.findOneById(RepoDataTests.CUSTOMER_1100005_ORG_ID);
    Assert.assertThat(true, Is.is(orgOrderSettings.isPresent()));
    final CustomerSettings settings = orgOrderSettings.get();
    Assert.assertThat(settings.getPaymentMethod().getId(), Is.is(1));
    Assert.assertThat(settings.getDeliveryId(), Is.is(2));
    Assert.assertThat(settings.getAllocationId(), Is.is(1));
    Assert.assertThat(settings.getCollectiveDelivery(), Is.is(1));
  }

  @Test
  public void givenOrgId_ShouldGetCustomerSettings() {
    final CustomerSettings custSettings = orgOrderSettingsRepo.findSettingsByOrgId(25);
    Assert.assertThat(custSettings, Matchers.notNullValue());
  }
}
