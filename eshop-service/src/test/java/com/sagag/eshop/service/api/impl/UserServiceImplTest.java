package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.LanguageService;
import com.sagag.eshop.service.api.RoleService;
import com.sagag.eshop.service.api.SalutationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.tests.utils.TestsConstants;
import com.sagag.eshop.service.tests.utils.TestsDataProvider;
import com.sagag.eshop.service.tests.utils.UserAssertions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Unit test class for User service.
 */
@EshopMockitoJUnitRunner
public class UserServiceImplTest {

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Mock
  private EshopUserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userServiceImpl;

  @Mock
  protected LanguageService languageService;

  @Mock
  protected LocaleContextHelper localeContextHelper;

  @Mock
  protected SalutationService salutationService;

  @Mock
  protected RoleService roleService;

  @Test
  public void givenUserIdShouldGetOne() {
    final EshopAuthority role = EshopAuthority.USER_ADMIN;
    final EshopUser user = TestsDataProvider.buildUser(role);
    when(userRepository.findUserByUserId(user.getId())).thenReturn(Optional.of(user));
    final EshopUser foundUser = userServiceImpl.getUserById(TestsConstants.USER_ID_LONG);
    verify(userRepository, times(1)).findUserByUserId(user.getId());
    UserAssertions.assertFoundUser(foundUser, role);
  }

  @Test(expected = UsernameNotFoundException.class)
  public void givenUserIdShouldNotFound() {
    final EshopAuthority role = EshopAuthority.USER_ADMIN;
    final EshopUser user = TestsDataProvider.buildUser(role);
    when(userRepository.findUserByUserId(user.getId())).thenReturn(Optional.empty());
    userServiceImpl.getUserById(TestsConstants.USER_ID_LONG);
    verify(userRepository, times(1)).findUserByUserId(user.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetUserProfile_shouldNotFound() {
    UserInfo userInfo = new UserInfo();
    userInfo.setId(TestsConstants.USER_ID_LONG);
    boolean isOtherProfile = false;
    when(userRepository.findById(userInfo.getId())).thenReturn(Optional.empty());
    userServiceImpl.getUserProfile(userInfo, isOtherProfile);
    verify(userRepository, times(1)).findById(userInfo.getId());
  }

  @Test
  public void testGetUserProfile_shouldFound() {
    final EshopAuthority role = EshopAuthority.USER_ADMIN;
    final EshopUser user = TestsDataProvider.buildUser(role);
    UserInfo userInfo = new UserInfo();
    userInfo.setId(TestsConstants.USER_ID_LONG);
    boolean isOtherProfile = false;
    when(userRepository.findById(userInfo.getId())).thenReturn(Optional.of(user));
    when(languageService.getAllLanguage()).thenReturn(Lists.emptyList());
    when(localeContextHelper.defaultAppLocaleLanguage()).thenReturn(StringUtils.EMPTY);
    when(salutationService.getProfileSalutations()).thenReturn(Lists.emptyList());
    when(roleService.getEshopRolesForUserProfile(user.getRoles().get(0), isOtherProfile)).thenReturn(Lists.emptyList());
    final UserProfileDto userProfile = userServiceImpl.getUserProfile(userInfo, isOtherProfile);
    verify(userRepository, times(1)).findById(userInfo.getId());
    Assert.assertEquals(userProfile.getId(), userInfo.getId().longValue());
  }

  @Test
  public void givenUsernameShouldGetAllUsers() {
    final EshopAuthority role = EshopAuthority.SYSTEM_ADMIN;
    final EshopUser user = TestsDataProvider.buildUser(role);
    final String userName = TestsConstants.USER_NAME;
    when(userRepository.findUsersByUsername(userName)).thenReturn(Arrays.asList(user));
    final List<EshopUser> foundUsers = userServiceImpl.getUsersByUsername(userName);
    verify(userRepository, times(1)).findUsersByUsername(userName);
    Assert.assertThat(foundUsers, Matchers.hasSize(1));
    UserAssertions.assertFoundUser(foundUsers.get(0), role);
  }
}
