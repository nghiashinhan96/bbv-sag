package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.tests.utils.TestsDataProvider;
import com.sagag.eshop.service.user.LoginInputType;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.hamcrest.Matchers;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EmailEshopUserFinderImplTest {

  @InjectMocks
  private EmailEshopUserFinderImpl eshopUserFinder;

  @Mock
  private OrganisationService orgService;

  @Mock
  private UserService userService;

  @Mock
  private EmailValidator emailValidator;

  @Test
  public void testFoundUserByEmail() throws UserValidationException {
    final String email = TestsDataProvider.EMAIL_USER_ADMIN;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.EMAIL));

    Mockito.when(emailValidator.isValid(Mockito.eq(email), Mockito.eq(null))).thenReturn(true);

    boolean isMatched = eshopUserFinder.isMatchedFinder(email, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    Mockito.when(userService.getUsersByEmail(Mockito.anyString()))
    .thenReturn(Arrays.asList(new EshopUser()));

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(email, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(true));

    Mockito.verify(emailValidator, Mockito.times(1)).isValid(Mockito.eq(email), Mockito.eq(null));
    Mockito.verify(userService, Mockito.times(1)).getUsersByEmail(email);
  }

  @Test(expected = UserValidationException.class)
  public void testFoundUserByEmailNotFoundAnyUsers() throws UserValidationException {
    final String email = TestsDataProvider.EMAIL_USER_ADMIN;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.EMAIL));

    Mockito.when(emailValidator.isValid(Mockito.eq(email), Mockito.eq(null))).thenReturn(true);

    boolean isMatched = eshopUserFinder.isMatchedFinder(email, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    Mockito.when(userService.getUsersByEmail(Mockito.anyString()))
    .thenReturn(Collections.emptyList());

    eshopUserFinder.findBy(email, affiliate);
  }

  @Test(expected = UserValidationException.class)
  public void testFoundUserByEmailWithMoreThanOneUser() throws UserValidationException {
    final String email = TestsDataProvider.EMAIL_USER_ADMIN;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.EMAIL));

    Mockito.when(emailValidator.isValid(Mockito.eq(email), Mockito.eq(null))).thenReturn(true);

    boolean isMatched = eshopUserFinder.isMatchedFinder(email, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    Mockito.when(userService.getUsersByEmail(Mockito.anyString()))
    .thenReturn(Arrays.asList(new EshopUser(), new EshopUser()));

    eshopUserFinder.findBy(email, affiliate);
  }

  @Test
  public void testFoundUserByEmailWithWssAff() throws UserValidationException {
    final String email = TestsDataProvider.EMAIL_USER_ADMIN;
    final String affiliate = TestsDataProvider.FINAL_WSS_AFF;

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.EMAIL));

    Mockito.when(emailValidator.isValid(Mockito.eq(email), Mockito.eq(null))).thenReturn(true);

    boolean isMatched = eshopUserFinder.isMatchedFinder(email, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    Mockito.verify(emailValidator, Mockito.times(1)).isValid(Mockito.eq(email), Mockito.eq(null));
  }

  @Test
  public void testFoundUserByEmailWithWrongEmailFormat() throws UserValidationException {
    final String email = TestsDataProvider.USERNAME_USER_ADMIN;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.EMAIL));

    Mockito.when(emailValidator.isValid(Mockito.eq(email), Mockito.eq(null))).thenReturn(false);

    boolean isMatched = eshopUserFinder.isMatchedFinder(email, affiliate);
    Assert.assertThat(isMatched, Matchers.is(false));

    Mockito.verify(emailValidator, Mockito.times(1)).isValid(Mockito.eq(email), Mockito.eq(null));
  }
}
