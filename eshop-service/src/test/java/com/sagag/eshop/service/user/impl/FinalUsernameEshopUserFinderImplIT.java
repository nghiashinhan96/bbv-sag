package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.tests.utils.TestsDataProvider;
import com.sagag.eshop.service.user.LoginInputType;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class FinalUsernameEshopUserFinderImplIT {

  @Autowired
  private FinalUsernameEshopUserFinderImpl eshopUserFinder;

  @Test
  public void testFinalUsernameEshopUserFinderImpl() throws UserValidationException {
    final String username = TestsDataProvider.FINAL_USER_NAME_WSS_USER_ADMIN;
    final String affiliate = TestsDataProvider.FINAL_WSS_AFF;

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.FINAL_CUSTOMER_USERNAME));

    boolean isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(username, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(true));
  }

  @Test
  public void testNotFoundFinalUsernameEshopUserFinderImpl() throws UserValidationException {
    final String username = TestsDataProvider.USERNAME_USER_ADMIN;
    final String affiliate = TestsDataProvider.FINAL_WSS_AFF;

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(username, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(false));
  }

}
