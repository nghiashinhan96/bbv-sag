package com.sagag.services.service.user.password;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.security.CompositePasswordEncoder;
import com.sagag.services.common.validator.password.PasswordFormatValidator;
import com.sagag.services.domain.eshop.dto.SytemAdminChangePasswordDto;
import com.sagag.services.service.mail.SimpleChangePasswordCriteria;
import com.sagag.services.service.mail.SimpleChangePasswordMailSender;
import com.sagag.services.service.user.password.change.SelfSysAdminEshopUserUpdatePasswordHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class SelfSysAdminEshopUserUpdatePasswordHandlerTest {

  @InjectMocks
  private SelfSysAdminEshopUserUpdatePasswordHandler handler;

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
  private PasswordFormatValidator passwordFormatValidator;

  @Mock
  private LocaleContextHelper localeContextHelper;

  private UserInfo userInfo;

  @Before
  public void setup() {
    userInfo = new UserInfo();
    userInfo.setId(1l);
  }

  @Test(expected = IllegalArgumentException.class)
  public void updatePassword_shouldThrowsException_givenModelMissingUserId() throws Exception {
    SytemAdminChangePasswordDto model = new SytemAdminChangePasswordDto();
    model.setPassword("123");
    model.setRedirectUrl("google.com");
    userInfo.setId(null);
    handler.updatePassword(userInfo, model);
  }

  @Test(expected = UserValidationException.class)
  public void updatePassword_shouldThrowsException_givenWrongFormatedPassword() throws Exception {
    SytemAdminChangePasswordDto model = new SytemAdminChangePasswordDto();
    model.setUserId(1L);
    model.setPassword("123");
    model.setRedirectUrl("google.com");
    handler.updatePassword(userInfo, model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void updatePassword_shouldThrowsException_givenModelMissingDefaultEmail()
    throws Exception {
    SytemAdminChangePasswordDto model = new SytemAdminChangePasswordDto();
    model.setUserId(1L);
    model.setPassword("admin123");
    model.setRedirectUrl("google.com");

    EshopUser eshopUser = new EshopUser(1L);
    when(userService.getUserById(1L)).thenReturn(eshopUser);
    Login login = new Login();
    when(loginService.getLoginForUser(1L)).thenReturn(login);
    doNothing().when(loginService).update(login);
    when(passwordFormatValidator.validate(anyString())).thenReturn(true);
    handler.updatePassword(userInfo, model);
  }

  @Test
  public void updatePassword_shouldSendEmailAfterUpdateSuccesfully_givenValidModel()
    throws Exception {
    SytemAdminChangePasswordDto model = new SytemAdminChangePasswordDto();
    model.setUserId(1L);
    model.setPassword("admin123");
    model.setRedirectUrl("google.com");

    EshopUser eshopUser = new EshopUser(1L);
    when(userService.getUserById(1L)).thenReturn(eshopUser);
    Login login = new Login();
    when(loginService.getLoginForUser(1L)).thenReturn(login);
    doNothing().when(loginService).update(login);

    when(passwordFormatValidator.validate(anyString())).thenReturn(true);
    when(orgCollectionService.findSettingValueByCollectionShortnameAndKey(Mockito.any(),
      Mockito.eq(SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName())))
      .thenReturn(Optional.of("sys.test.01.ch"));
    doNothing().when(changePasswordMailSender).send(any(SimpleChangePasswordCriteria.class));
    handler.updatePassword(userInfo, model);

    verify(changePasswordMailSender, times(1)).send(any(SimpleChangePasswordCriteria.class));
  }

}
