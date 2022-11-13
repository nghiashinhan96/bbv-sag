package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for {@link ExternalOrganisationRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class ExternalOrganisationRepositoryIT {

  @Autowired
  private ExternalOrganisationRepository externalOrgRepo;

  @Test
  public void testIsCustomerNameExisted() {
    final String exitedCustomerName = "9A339B241B";
    final boolean isExist = externalOrgRepo.isCustomerNameExisted(exitedCustomerName);
    Assert.assertTrue(isExist);
  }

  @Test
  public void testIsCustomerNameExistedNotFound() {
    final String notExitedCustomerName = "ABCDEFG";
    final boolean isExist = externalOrgRepo.isCustomerNameExisted(notExitedCustomerName);
    Assert.assertFalse(isExist);
  }
}
