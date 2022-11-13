package com.sagag.services.dvse.authenticator.mdm;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.dvse.DvseApplication;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.wsdl.dvse.User;
import com.sagag.services.hazelcast.api.UserCacheService;

/**
 * IT for {@link ConnectDvseUserAuthenticator}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { DvseApplication.class })
@EshopIntegrationTest
@Transactional
public class ConnectDvseUserAuthenticatorIT {

  @Autowired
  private ConnectDvseUserAuthenticator authenticator;

  @Autowired
  private UserCacheService userCacheService;

  @Test(expected = AccessDeniedException.class) @Ignore
  public void shouldThrowAccessDeniedExceptioWithUserNotStoreInCache() {
    final User mdmUser = new User();
    mdmUser.setUsername("1F255CF3F5337E5F");
    mdmUser.setPassword("31D7F6939DA6EC14");

    authenticator.authenticate(mdmUser);
  }

  @Test
  public void shouldAuthenticateSuccesfully() {
    final long expectedUserId = 27L; // user name: tuan1.ax
    final int userSettingId = 33;
    mockLoginConnectApp(expectedUserId, userSettingId);

    final User mdmUser = createMdmUserLoginRequest();
    final ConnectUser connectUser = authenticator.authenticate(mdmUser);

    Assert.assertThat(connectUser, Matchers.notNullValue());
    Assert.assertThat(connectUser.getId(), Matchers.equalTo(expectedUserId));
  }

  private void mockLoginConnectApp(final long userId, final int userSettingId) {
    final UserInfo user = new UserInfo();
    user.setId(userId);
    final OwnSettings settings = new OwnSettings();
    final UserSettings userSettings = new UserSettings();
    userSettings.setId(userSettingId);
    settings.setUserSettings(userSettings);
    user.setSettings(settings);
    userCacheService.put(user);
  }

  private User createMdmUserLoginRequest() {
    final User mdmUser = new User();
    mdmUser.setUsername("CA23FD8D4FE639E7");
    mdmUser.setPassword("62CEC04B112B70F1");
    return mdmUser;
  }

}
