package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for {@link VBasketHistoryRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class VBasketHistoryRepositoryIT {

  @Autowired
  private VBasketHistoryRepository repository;

  @Test
  public void testCountByOrganisationOrgCode() {
    Long count = repository.countByOrganisationOrgCode("1100005");
    Assert.assertThat(count, Matchers.greaterThanOrEqualTo(0L));
  }

}
