package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.LicenseType;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;

import org.apache.commons.lang3.math.NumberUtils;
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
 * Integration test class for License settings repository.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class LicenseSettingsRepositoryIT {

  @Autowired
  private LicenseSettingsRepository licenseSettingsRepo;

  @Test
  public void givenVinType_ShouldGetAllVinLicenses() {
    final List<LicenseSettingsDto> vinLicenses =
        licenseSettingsRepo.searchLicenses(LicenseType.VIN.name());
    Assert.assertThat(vinLicenses, Matchers.notNullValue());
    Assert.assertThat(vinLicenses.size(), Matchers.greaterThanOrEqualTo(NumberUtils.INTEGER_ONE));
  }
}
