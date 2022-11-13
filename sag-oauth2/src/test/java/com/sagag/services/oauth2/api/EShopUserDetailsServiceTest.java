package com.sagag.services.oauth2.api;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.oauth2.api.impl.user.EShopUserDetailsServiceImpl;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.processor.CompositeUserAuthenticationProcessor;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * UT for @{link EShopUserDetailsService}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EShopUserDetailsServiceTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private EShopUserDetailsServiceImpl userDetailsServiceImpl;

  @Mock
  private HttpServletRequest request;

  @Mock
  private OrganisationService orgService;

  @Mock
  private CustomerExternalService custExternalService;

  @Mock
  private CustomerSettingsRepository customerSettingsRepo;

  @Mock
  private PaymentMethodRepository paymentRepo;

  @Mock
  private DeliveryTypesRepository deliveryTypesRepo;

  @Mock
  private TokenStore tokenStore;

  @Mock
  private UserSearchFactory userSearchFactory;

  @Mock
  private EshopUser foundUser;

  @Mock
  private OAuth2Authentication auth2Authentication;

  @Mock
  private LoginService loginService;

  @Mock
  private UserSettingsRepository userSettingsRepo;

  @Mock
  private EshopUser user;

  @Mock
  private CustomerCacheService customerCacheService;

  @Mock
  private EshopAuthHelper eshopAuthenticationHelper;

  @Mock
  private CompositeUserAuthenticationProcessor processor;

  @Test(expected = UsernameNotFoundException.class)
  public void shouldThrowErrorIfNotFoundAnyEshopUser() throws UserValidationException {

    when(userSearchFactory.searchUsernameCaseSensitive(anyString(), anyString()))
    .thenThrow(new UserValidationException(UserErrorCase.UE_NFU_001, StringUtils.EMPTY));
    when(eshopAuthenticationHelper.getLoginAffiliate()).thenReturn("derendinger-at");

    userDetailsServiceImpl.loadUserByUsername("nu1.ga");
  }

  @Test
  @Ignore
  public void shouldMakeUseOfCacheCustomerInfoCache_NEVER() throws UserValidationException {
    String username = "nu1.ga";
    final long userId = 26L;
    EshopUser eshopUser = DataProvider.buildActiveEshopUser(userId, username,
        EshopAuthority.SALES_ASSISTANT.name());
    eshopUser.setUsername(username);
    when(userSearchFactory.searchUsernameCaseSensitive(anyString(), anyString()))
    .thenReturn(Optional.of(eshopUser));
    when(eshopAuthenticationHelper.getLoginAffiliate()).thenReturn("derendinger-at");
    when(eshopAuthenticationHelper.getLoginSalesToken()).thenReturn("");
    when(loginService.getLoginForUser(userId)).thenReturn(eshopUser.getLogin());
    userDetailsServiceImpl.loadUserByUsername(username);

  }

}
