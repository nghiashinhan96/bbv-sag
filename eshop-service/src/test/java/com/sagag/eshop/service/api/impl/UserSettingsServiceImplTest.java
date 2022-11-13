package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.UserSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

/**
 * UT to verify for {@link UserSettingsServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserSettingsServiceImplTest {

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Mock
  private UserSettingsRepository userSettingsRepository;

  @InjectMocks
  private UserSettingsServiceImpl userSettingsService;

  @Test
  public void findUserSettingsById() {

    // Expected objects
    int id = 3;

    UserSettings expectedUserSettings = new UserSettings();
    expectedUserSettings.setId(id);

    // mock role repo
    when(userSettingsRepository.findOneById(expectedUserSettings.getId())).thenReturn(
        Optional.of(expectedUserSettings));

    UserSettings orgOrderSettings =
        userSettingsService.findUserSettingsById(expectedUserSettings.getId()).get();

    assertNotNull(orgOrderSettings);
    assertEquals(expectedUserSettings.getId(), orgOrderSettings.getId());
    assertEquals(expectedUserSettings.getAllocationId(), orgOrderSettings.getAllocationId());
    assertEquals(expectedUserSettings.getDeliveryId(), orgOrderSettings.getDeliveryId());
    assertEquals(expectedUserSettings.getCollectiveDelivery(),
        orgOrderSettings.getCollectiveDelivery());

    // make sure method is invoked
    verify(userSettingsRepository).findOneById(expectedUserSettings.getId());
  }
}
