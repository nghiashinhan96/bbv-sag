package com.sagag.services.admin.controller;

import static org.mockito.Mockito.when;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Getter;

import org.junit.Before;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public abstract class AbstractControllerTest {


  @Mock
  @Getter
  private OAuth2Authentication oAuth2Authentication;

  @Mock
  private Authentication authentication;

  @Mock
  @Getter
  private UserInfo user;

  /**
   * Sets up the pre-condition for testing.
   *
   * @throws Exception throws when program fails.
   */
  @Before
  public void setUp() throws Exception {
    when(oAuth2Authentication.getUserAuthentication()).thenReturn(authentication);
    when(authentication.getDetails()).thenReturn(user);
    when(user.getCustomer()).thenReturn(new Customer());
  }

}
