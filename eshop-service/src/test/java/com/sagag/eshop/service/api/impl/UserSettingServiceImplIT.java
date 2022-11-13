package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * Class integration test for {@link UserSettingsService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
public class UserSettingServiceImplIT {

  @Autowired
  private UserSettingsService userSettingsService;

  @Test
  public void testGetUserSettingWithViewBillingForAdmin() {
    Optional<UserSettings> userSettings = userSettingsService.findUserSettingsById(1063);
    Assert.assertThat(userSettings.isPresent(), Matchers.equalTo(true));
    Assert.assertThat(userSettings.get().isViewBilling(), Matchers.equalTo(false));
    UserSettingsDto settings = userSettingsService.getUserSettings(new CustomerSettings(), 10057L,
        EshopAuthority.USER_ADMIN.name());
    Assert.assertNotNull(settings);
    Assert.assertThat(settings.getPriceSettings().isAllowViewBillingChanged(),
        Matchers.equalTo(false));
    Assert.assertThat(settings.isViewBilling(), Matchers.equalTo(true));
  }
}
