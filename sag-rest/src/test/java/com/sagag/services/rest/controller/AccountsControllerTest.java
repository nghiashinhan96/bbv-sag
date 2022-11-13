package com.sagag.services.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.enums.LinkPartnerEnum;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.rest.controller.account.AccountsController;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.validator.EshopUserFromAxServiceValidator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

/**
 * Unit tests class for Accounts REST APIs.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountsControllerTest {

  private static final long SALES_ID_STR = 5;

  private static final String DVSE_CATALOG_URI = "http://dvse.catalog.uri";

  @InjectMocks
  private AccountsController accountsController;

  @Mock
  private UserService userService;

  @Mock
  private OAuth2Authentication authedUser;

  @Mock
  private UserBusinessService userBusinessService;

  @Mock
  private EshopUserFromAxServiceValidator validator;

  private MockMvc mockMvc;

  /**
   * Sets up the resources for testing.
   *
   * @throws Exception throws when program fails.
   */
  @Before
  public void setUp() throws Exception {
    mockMvc = standaloneSetup(accountsController).build();
    when(authedUser.getName()).thenReturn("long.nguyen");
    when(authedUser.getPrincipal()).thenReturn(mockUser());

  }

  private static UserInfo mockUser() {
    final UserInfo user = new UserInfo();
    user.setSalesId(SALES_ID_STR);
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    final OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(new Customer());
    ownSettings.setUserSettings(new UserSettings());
    ownSettings.addExternalUrls(LinkPartnerEnum.DVSE, DVSE_CATALOG_URI);
    user.setSettings(ownSettings);
    return user;
  }

  @Test
  public void testGetUserDetailUrlNotFound() throws Exception {
    mockMvc.perform(get("/userdetail")).andExpect(status().isNotFound());
  }

  @Test
  public void testGetUserDetailMethodNotAllowed() throws Exception {
    mockMvc.perform(post("/user/detail")).andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void testGetUserDetailSuccessful() throws Exception {
    mockMvc.perform(get("/user/detail").principal(authedUser)).andExpect(status().isOk());
  }

  @Test
  public void testCreateUserByAdmin() throws Exception {
    doNothing().when(userBusinessService).createUserByAdmin(any(UserInfo.class),
        any(UserProfileDto.class));
    mockMvc
        .perform(post("/customer/users/create")
            .content(new ObjectMapper().writeValueAsString(new UserProfileDto()))
            .principal(authedUser).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
    verify(userBusinessService, times(1)).createUserByAdmin(any(UserInfo.class),
        any(UserProfileDto.class));
  }

}
