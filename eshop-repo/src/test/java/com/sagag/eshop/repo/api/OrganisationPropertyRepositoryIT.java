package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.OrganisationProperty;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link OrganisationPropertyRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OrganisationPropertyRepositoryIT {

  @Autowired
  private OrganisationPropertyRepository organisationPropertyRepo;

  @Test
  public void testFindByCustomerNumber() {
    final List<OrganisationProperty> orders =
        organisationPropertyRepo.findByOrganisationId(Long.valueOf(RepoDataTests.CUSTOMER_1100005_ORG_ID));
    Assert.assertThat(orders, Matchers.not(Matchers.empty()));
  }
}
