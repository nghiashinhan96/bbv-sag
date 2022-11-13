package com.sagag.services.service.user.password;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.security.CompositePasswordEncoder;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.SytemAdminResetPasswordDto;
import com.sagag.services.service.mail.SimpleChangePasswordCriteria;
import com.sagag.services.service.mail.SimpleChangePasswordMailSender;
import com.sagag.services.service.user.password.reset.SelfSysAdminResetPasswordHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SelfSysAdminResetPasswordHandlerTest {

  @InjectMocks
  private SelfSysAdminResetPasswordHandler handler;

  @Mock
  private OrganisationService organisationService;

  @Mock
  private UserService userService;

  @Mock
  private LoginService loginService;

  @Mock
  private SimpleChangePasswordMailSender changePasswordMailSender;

  @Mock
  private UserSearchFactory userSearchFactory;

  @Mock
  private CompositePasswordEncoder passwordEncoder;

  @Mock
  private OrganisationCollectionService orgCollectionService;

  @Mock
  private LocaleContextHelper localeContextHelper;

  @Test(expected = UserValidationException.class)
  public void resetPassword_shoulThrowsException_givenModelMissingEmail() throws Exception {
    SytemAdminResetPasswordDto dto = new SytemAdminResetPasswordDto();
    handler.handle(dto);
  }

  @Test(expected = UserValidationException.class)
  public void resetPassword_shoulThrowsException_givenNotFoundUser() throws Exception {
    SytemAdminResetPasswordDto dto = new SytemAdminResetPasswordDto();
    dto.setEmail("sys01.admin@bbv.ch");
    when(userSearchFactory.searchEshopUserByInput("sys01.admin@bbv.ch", "sag"))
      .thenReturn(Optional.empty());
    handler.handle(dto);
  }

  @Test
  public void resetPassword_shouldResetPassSucessfully_givenValidData() throws Exception {
    SytemAdminResetPasswordDto dto = new SytemAdminResetPasswordDto();
    dto.setEmail("sys01.admin@bbv.ch");
    dto.setRedirectUrl("google.com");

    EshopRole role = new EshopRole();
    role.setName(EshopAuthority.SYSTEM_ADMIN.name());
    GroupRole groupRole = new GroupRole();
    groupRole.setEshopRole(role);
    EshopGroup group = new EshopGroup();
    group.setGroupRoles(Arrays.asList(groupRole));
    GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(group);

    Login login = new Login();
    EshopUser eshopUser = new EshopUser(1L);
    eshopUser.setLogin(login);
    eshopUser.setGroupUsers(Arrays.asList(groupUser));
    when(userSearchFactory.searchEshopUserByInput("sys01.admin@bbv.ch", "sag"))
      .thenReturn(Optional.of(eshopUser));
    when(orgCollectionService.findSettingValueByCollectionShortnameAndKey(Mockito.any(),
      Mockito.eq(SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName())))
      .thenReturn(Optional.of("sys.test.01.ch"));
    doNothing().when(loginService).update(login);
    doNothing().when(changePasswordMailSender).send(any(SimpleChangePasswordCriteria.class));
    handler.handle(dto);
  }
}
