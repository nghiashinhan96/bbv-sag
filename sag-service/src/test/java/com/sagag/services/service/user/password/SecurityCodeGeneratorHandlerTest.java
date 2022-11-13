package com.sagag.services.service.user.password;

import com.sagag.eshop.repo.entity.*;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.SecurityCodeRequestDto;
import com.sagag.services.service.mail.ChangePasswordCriteria;
import com.sagag.services.service.mail.ChangePasswordMailSender;
import com.sagag.services.service.user.password.reset.SecurityCodeGeneratorHandler;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecurityCodeGeneratorHandlerTest {

  @InjectMocks
  private SecurityCodeGeneratorHandler handler;

  @Mock
  private OrganisationService organisationService;

  @Mock
  private ChangePasswordMailSender changePasswordMailSender;

  @Mock
  private UserSearchFactory userSearchFactory;

  @Mock
  private OrganisationCollectionService orgCollectionService;

  @Mock
  private ChangePasswordPermissionEvaluator changePasswordPermissionEvaluator;

  @Mock
  private SecurityCodeGenerator generator;

  @Mock
  private LocaleContextHelper localeContextHelper;

  @Test(expected = UserValidationException.class)
  public void generateSecurityCodeShouldThrowsExceptionGivenModelMissingReqDto() throws Exception {
    SecurityCodeRequestDto dto = new SecurityCodeRequestDto();
    handler.handle(dto);
  }

  @Test(expected = UserValidationException.class)
  public void generateSecurityCodeShouldThrowsExceptionGivenNotFoundUser() throws Exception {
    SecurityCodeRequestDto dto = new SecurityCodeRequestDto();
    dto.setUsername("sys01.admin@bbv.ch");
    dto.setAffiliateId("sag");
    when(userSearchFactory.searchEshopUserByInput(dto.getUsername(), dto.getAffiliateId()))
      .thenReturn(Optional.empty());
    handler.handle(dto);
  }

  @Test
  public void generateSecurityCodeShouldSuccessfully() throws Exception {
    SecurityCodeRequestDto dto = new SecurityCodeRequestDto();
    dto.setUsername("sys01.admin@bbv.ch");
    dto.setAffiliateId("sag");
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

    PasswordResetToken resetToken = new PasswordResetToken();
    resetToken.setToken("1234");
    resetToken.setHashUsernameCode("####");

    when(userSearchFactory.searchEshopUserByInput(dto.getUsername(), dto.getAffiliateId()))
      .thenReturn(Optional.of(eshopUser));
    doNothing().when(changePasswordPermissionEvaluator).check(eshopUser, dto.getAffiliateId());
    when(organisationService.findOrgSettingByKey("sag",SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName()))
            .thenReturn(Optional.of(StringUtils.EMPTY));

    when(generator.generateCode(eshopUser)).thenReturn(resetToken);
    when(orgCollectionService.findSettingValueByCollectionShortnameAndKey(Mockito.any(),
      Mockito.eq(SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName())))
      .thenReturn(Optional.of("sys.test.01.ch"));
    doNothing().when(changePasswordMailSender).send(any(ChangePasswordCriteria.class));
   String result = handler.handle(dto);
   Assert.assertEquals("####", result);
  }
}
