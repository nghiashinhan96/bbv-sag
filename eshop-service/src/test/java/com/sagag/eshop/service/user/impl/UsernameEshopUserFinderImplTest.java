package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.tests.utils.TestsConstants;
import com.sagag.eshop.service.tests.utils.TestsDataProvider;
import com.sagag.eshop.service.user.LoginInputType;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;

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
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UsernameEshopUserFinderImplTest {

  @InjectMocks
  private UsernameEshopUserFinderImpl eshopUserFinder;

  @Mock
  private UserService userService;

  @Mock
  private EmailValidator emailValidator;

  @Mock
  private OrganisationService orgService;

  @Mock
  private OrganisationCollectionService orgCollectionService;

  @Test
  public void shouldFoundUserAdminByUsername() throws UserValidationException {
    final String username = TestsConstants.USER_NAME;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    this.testFoundUserByUsername(username, affiliate, EshopAuthority.USER_ADMIN);

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(1)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(1)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(1)).getByOrgId(Mockito.anyInt());
  }

  @Test
  public void shouldFoundSaleByUsername() throws UserValidationException {
    final String username = TestsConstants.USER_NAME;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    this.testFoundUserByUsername(username, affiliate, EshopAuthority.SALES_ASSISTANT);

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(0)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(0)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(0)).getByOrgId(Mockito.anyInt());
  }

  @Test
  public void shouldFoundSysAdminBelongToSagByUsernameContainSagAff() throws UserValidationException {
    final String username = TestsConstants.USER_NAME;
    final String affiliate = SagConstants.SAG;

    this.testFoundUserByUsername(username, affiliate, EshopAuthority.SYSTEM_ADMIN);

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(0)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(0)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(0)).getByOrgId(Mockito.anyInt());
  }

  @Test
  public void shouldFoundSysAdminBelongToSagByUsernameEmptyAff() throws UserValidationException {
    final String username = TestsConstants.USER_NAME;
    final String affiliate = StringUtils.EMPTY;

    this.testFoundUserByUsername(username, affiliate, EshopAuthority.SYSTEM_ADMIN);

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(0)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(0)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(0)).getByOrgId(Mockito.anyInt());
  }

  @Test
  public void shouldFoundSysAdminBelongToSagByUsernameUserDdatAff() throws UserValidationException {
    final String username = TestsConstants.USER_NAME;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    this.testFoundUserByUsername(username, affiliate, EshopAuthority.SYSTEM_ADMIN);

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(1)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(0)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(0)).getByOrgId(Mockito.anyInt());
  }

  @Test
  public void shouldFoundGroupAdminBelongToSagByUsernameUserDdatAff() throws UserValidationException {
    final String username = TestsConstants.USER_NAME;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    this.testFoundUserByUsername(username, affiliate, EshopAuthority.GROUP_ADMIN);

    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
    Mockito.verify(orgService, Mockito.times(1)).getFirstByUserId(Mockito.any());
    Mockito.verify(orgCollectionService, Mockito.times(0)).getCollectionByOrgId(Mockito.any());
    Mockito.verify(orgService, Mockito.times(0)).getByOrgId(Mockito.anyInt());
  }

  private void testFoundUserByUsername(String username, String affiliate, EshopAuthority authority)
      throws UserValidationException {
    LoginInputType inputType = eshopUserFinder.inputType();
    Assert.assertThat(inputType, Matchers.is(LoginInputType.USERNAME));

    boolean isMatched = eshopUserFinder.isMatchedFinder(username, affiliate);
    Assert.assertThat(isMatched, Matchers.is(true));

    final EshopUser user = TestsDataProvider.buildUser(authority);
    Mockito.when(userService.getUsersByUsername(Mockito.eq(username)))
    .thenReturn(Arrays.asList(new EshopUser(), user));

    Organisation orgLv1 = new Organisation();
    orgLv1.setShortname(affiliate);
    Mockito.when(orgService.getFirstByUserId(Mockito.any()))
    .thenReturn(Optional.of(orgLv1));

    Mockito.when(orgCollectionService.getCollectionByOrgId(Mockito.any()))
    .thenReturn(Optional.of(new OrganisationCollection()));

    Organisation orgLv2 = new Organisation();
    orgLv2.setShortname(affiliate);
    Mockito.when(orgService.getByOrgId(Mockito.anyInt()))
    .thenReturn(Optional.of(orgLv2));

    Optional<EshopUser> userOpt = eshopUserFinder.findBy(username, affiliate);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(true));
  }

  @Test(expected = UserValidationException.class)
  public void shouldThrowExceptionWithEmptyUsername() throws UserValidationException {
    final String username = StringUtils.EMPTY;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    eshopUserFinder.findBy(username, affiliate);
  }

  @Test(expected = UserValidationException.class)
  public void shouldNotFoundAnyUsers() throws UserValidationException {
    final String username = TestsConstants.USER_NAME;
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();

    Mockito.when(userService.getUsersByUsername(Mockito.eq(username)))
    .thenReturn(Collections.emptyList());
    eshopUserFinder.findBy(username, affiliate);
    Mockito.verify(userService, Mockito.times(1)).getUsersByUsername(Mockito.eq(username));
  }
}
