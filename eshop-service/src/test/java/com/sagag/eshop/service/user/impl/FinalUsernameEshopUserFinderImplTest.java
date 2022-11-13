package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.tests.utils.TestsDataProvider;
import com.sagag.eshop.service.user.LoginInputType;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FinalUsernameEshopUserFinderImplTest {

  @InjectMocks
  private FinalUsernameEshopUserFinderImpl eshopUserFinder;

  @Mock
  private UserService userService;

  @Mock
  private EmailValidator emailValidator;

  @Mock
  private OrganisationService orgService;

  @Mock
  private OrganisationCollectionService orgCollectionService;

  @Test
  public void shouldReturnWholesalerEshopUser() throws UserValidationException {
    final String username = TestsDataProvider.FINAL_USER_NAME_WSS_USER_ADMIN;
    final String affiliate = TestsDataProvider.FINAL_WSS_AFF;

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.FINAL_CUSTOMER_USERNAME));

    boolean isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    final EshopUser user = TestsDataProvider.buildWholesalerUserAdmin();
    Mockito.when(userService.getUsersByUsername(Mockito.eq(username)))
    .thenReturn(Arrays.asList(user));

    Mockito.when(orgService.getFirstByUserId(Mockito.any()))
    .thenReturn(Optional.of(new Organisation()));

    Mockito.when(orgCollectionService.getCollectionByOrgId(Mockito.any()))
    .thenReturn(Optional.of(new OrganisationCollection()));

    Mockito.when(orgService.getByOrgId(Mockito.anyInt()))
    .thenReturn(Optional.of(new Organisation()));

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(username, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(true));

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(1)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(1)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(2)).getByOrgId(Mockito.anyInt());
  }

  @Test
  public void shouldNotFoundCollectionOfWholesalerEshopUser() throws UserValidationException {
    final String username = TestsDataProvider.FINAL_USER_NAME_WSS_USER_ADMIN;
    final String affiliate = TestsDataProvider.FINAL_WSS_AFF;

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.FINAL_CUSTOMER_USERNAME));

    boolean isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    final EshopUser user = TestsDataProvider.buildWholesalerUserAdmin();
    Mockito.when(userService.getUsersByUsername(Mockito.eq(username)))
    .thenReturn(Arrays.asList(user));

    Mockito.when(orgService.getFirstByUserId(Mockito.any()))
    .thenReturn(Optional.of(new Organisation()));

    Mockito.when(orgCollectionService.getCollectionByOrgId(Mockito.any()))
    .thenReturn(Optional.empty());

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(username, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(false));

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(1)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(1)).getCollectionByOrgId(Mockito.any());
  }

  @Test
  public void shouldFoundOrgContainOrgCodeOfWholesalerEshopUser() throws UserValidationException {
    final String username = TestsDataProvider.FINAL_USER_NAME_WSS_USER_ADMIN;
    final String affiliate = TestsDataProvider.FINAL_WSS_AFF;

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.FINAL_CUSTOMER_USERNAME));

    boolean isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    final EshopUser user = TestsDataProvider.buildWholesalerUserAdmin();
    Mockito.when(userService.getUsersByUsername(Mockito.eq(username)))
    .thenReturn(Arrays.asList(user));

    Organisation org = new Organisation();
    org.setOrgCode("1234567");
    Mockito.when(orgService.getFirstByUserId(Mockito.any()))
    .thenReturn(Optional.of(org));

    Mockito.when(orgCollectionService.getCollectionByOrgId(Mockito.any()))
    .thenReturn(Optional.of(new OrganisationCollection()));

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(username, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(false));

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(1)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(1)).getCollectionByOrgId(Mockito.any());
  }

  @Test
  public void shouldNotFoundAffiliateOfWholesalerEshopUser() throws UserValidationException {
    final String username = TestsDataProvider.FINAL_USER_NAME_WSS_USER_ADMIN;
    final String affiliate = TestsDataProvider.FINAL_WSS_AFF;

    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.FINAL_CUSTOMER_USERNAME));

    boolean isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    final EshopUser user = TestsDataProvider.buildWholesalerUserAdmin();
    Mockito.when(userService.getUsersByUsername(Mockito.eq(username)))
    .thenReturn(Arrays.asList(user));

    Mockito.when(orgService.getFirstByUserId(Mockito.any()))
    .thenReturn(Optional.of(new Organisation()));

    Mockito.when(orgCollectionService.getCollectionByOrgId(Mockito.any()))
    .thenReturn(Optional.of(new OrganisationCollection()));

    Mockito.when(orgService.getByOrgId(Mockito.anyInt()))
    .thenReturn(Optional.empty());

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(username, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(false));

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(1)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(1)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(1)).getByOrgId(Mockito.anyInt());
  }

  @Test
  public void shouldValidateFailedFinderWithMultipleCases() {
    String username = TestsDataProvider.FINAL_USER_NAME_WSS_USER_ADMIN;
    String affiliate = SupportedAffiliate.WBB.getAffiliate();
    boolean isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(false));

    username = StringUtils.EMPTY;
    affiliate = StringUtils.EMPTY;
    isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(false));

    username = "test@gmail.com";
    affiliate = TestsDataProvider.FINAL_WSS_AFF;
    Mockito.when(emailValidator.isValid(Mockito.eq(username), Mockito.eq(null))).thenReturn(true);
    isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(false));
    Mockito.verify(emailValidator, Mockito.times(1)).isValid(Mockito.eq(username), Mockito.eq(null));

  }
}
