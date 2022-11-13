package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.tests.utils.TestsDataProvider;
import com.sagag.eshop.service.user.LoginInputType;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;

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
public class EmailEshopUserFinderImplIT {

  @Autowired
  private EmailEshopUserFinderImpl eshopUserFinder;

  @Test
  public void testEmailEshopUserFinderImpl() throws UserValidationException {
    final String email = TestsDataProvider.EMAIL_USER_ADMIN;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.EMAIL));

    boolean isMatched = eshopUserFinder.isMatchedFinder(email, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(email, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(true));
  }

  @Test(expected = UserValidationException.class)
  public void testNotFoundEmailEshopUserFinderImpl() throws UserValidationException {
    final String email = "admin@derendinger.at1";
    final String affiliate = SupportedAffiliate.DERENDINGER_CH.getAffiliate();

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.EMAIL));

    boolean isMatched = eshopUserFinder.isMatchedFinder(email, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    eshopUserFinder.findBy(email, affiliate);
  }
}
