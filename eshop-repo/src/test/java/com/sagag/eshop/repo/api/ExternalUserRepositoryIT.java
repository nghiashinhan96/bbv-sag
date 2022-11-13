package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.ExternalApp;

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
 * Integration test class for {@link ExternalUserRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class ExternalUserRepositoryIT {

  @Autowired
  private ExternalUserRepository externalUserRepo;

  @Test
  public void testFindFirstByEshopUserId() {
    final Long eshopUserId = Long.valueOf(26);
    final Optional<ExternalUser> user =
        externalUserRepo.findFirstByEshopUserIdAndExternalApp(eshopUserId, ExternalApp.DVSE);
    Assert.assertThat(true, Is.is(user.isPresent()));
    Assert.assertThat(eshopUserId, Is.is(user.get().getEshopUserId()));
  }

  @Test
  public void testFindFirstByEshopUserIdWithNotFoundAnyResults() {
    final Long eshopUserId = Long.valueOf(1);
    final Optional<ExternalUser> user =
        externalUserRepo.findFirstByEshopUserIdAndExternalApp(eshopUserId, ExternalApp.DVSE);
    Assert.assertThat(false, Is.is(user.isPresent()));
  }

  @Test
  public void testIsCustomerNameExisted() {
    final String exitedUsername = "1F255CF3F5337E5F";
    final boolean isExist = externalUserRepo.isUsernameExisted(exitedUsername);
    Assert.assertTrue(isExist);
  }

  @Test
  public void testIsUserNameExisted_Notfound() {
    final String notExitedUsername = "ABCDEF";
    final boolean isExist = externalUserRepo.isUsernameExisted(notExitedUsername);
    Assert.assertFalse(isExist);
  }

}
