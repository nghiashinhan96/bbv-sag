package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link UserSettingsRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class UserSettingsRepositoryIT {

  @Autowired
  private UserSettingsRepository repository;

  @Test
  public void testFindByUserIdValid() {
    final Optional<UserSettings> organisationSettings = repository.findByUserId(RepoDataTests.ADMIN_USER_ID);
    assertTrue(organisationSettings.isPresent());
    // Note: the value may change when db is changed
    final UserSettings userSettings = organisationSettings.get();
    assertEquals("CREDIT", userSettings.getPaymentMethod().getDescCode());
    assertEquals(1L, userSettings.getPaymentMethod().getId());
  }

  @Test
  public void testFindByUserIdInvalid() {
    final Optional<UserSettings> organisationSettings = repository.findByUserId(999L);
    assertFalse(organisationSettings.isPresent());
  }

  @Test
  public void findActiveUserSettingsByOrgId_shouldGetUserSettings_givenOrgId() throws Exception {
    List<UserSettings> settings = repository.findActiveUserSettingsByOrgId(16).get();
    assertNotNull(settings);
  }
}
