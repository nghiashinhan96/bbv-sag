package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.License;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.LicenseType;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for License repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class LicenseRepositoryIT {

  private static final long CUSTOMER_NUMBER = RepoDataTests.CUSTOMER_1100005_ORG_CODE;

  private static final int VIN_CALL_LEFT = 500;

  @Autowired
  private LicenseRepository licenseRepo;

  @Test
  public void testFindAllByCustomerNrAndTypeOfLicense() {
    List<License> licenses = licenseRepo.findAllByCustomerNrAndTypeOfLicense(469743, "VIN");
    Assert.assertNotNull(licenses);
  }

  @Test
  public void testFindAllHaynesProLicenseByCustomerNr() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    Page<License> licenses =
        licenseRepo.findAvailableHaynesProLicense(CUSTOMER_NUMBER, pageRequest);
    Assert.assertNotNull(licenses.getContent());
  }

  @Test
  public void givenCustomerShouldGetAvailableVinCalls() {
    Integer calls = licenseRepo.findAvailableLicenseCalls(CUSTOMER_NUMBER, LicenseType.VIN.name());
    Assert.assertThat(VIN_CALL_LEFT, Matchers.is(calls));
  }

  @Test
  public void givenUnknownCustomerShouldGetNullAvailableVinCalls() {
    Integer calls = licenseRepo.findAvailableLicenseCalls(999999L, LicenseType.VIN.name());
    Assert.assertThat(calls, Matchers.nullValue());
  }
}
