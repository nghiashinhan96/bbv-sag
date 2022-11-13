package com.sagag.eshop.service.api.sso;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileResponseDto;
import com.sagag.eshop.service.sso.SsoLoginProfileServiceCaseOne;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SsoLoginProfileServiceCaseOneTest extends AbstractSsoLoginProfileServiceTest {

  @InjectMocks
  protected SsoLoginProfileServiceCaseOne ssoLoginProfileServiceCaseOne;

  @Mock
  protected UserService userService;

  @Test
  public void process_shouldUpdateUuid_givenOldSalesAccountWithUuidIsUpdated() throws Exception {
    SsoLoginProfileRequestDto request = buildRequest();
    String updatedUuid = "effbd2a4-3e31-4090-8b9b-18e64d1b49dc-updated";
    request.setUuid(updatedUuid);

    AadAccounts aadAccounts = mockAadAccount();
    when(axAccountService.searchSaleAccount(EMAIL)).thenReturn(Optional.of(aadAccounts));
    when(userService.hasRoleByUsername(USERNAME, EshopAuthority.SALES_ASSISTANT)).thenReturn(true);
    ssoLoginProfileServiceCaseOne.process(request);
    assertThat(aadAccounts.getUuid(), Matchers.is(updatedUuid));
  }

  @Test
  public void process_shouldUpdateEmail_giveExistingAccountHadBeenChangedEmail() throws Exception {
    SsoLoginProfileRequestDto request = buildRequest();
    String newEmail = "newEmail@bbv.ch";
    request.setEmail(newEmail);
    request.setUserName(newEmail);
    when(axAccountService.searchSaleAccount(newEmail)).thenReturn(Optional.empty());


    AadAccounts aadAccounts = mockAadAccount();
    when(aadAccountsRepo.findByUuid(UUID)).thenReturn(Optional.of(aadAccounts));

    ssoLoginProfileServiceCaseOne.process(request);
    verify(aadAccountsService, times(1)).updateEmail(aadAccounts, newEmail);
  }

  @Test
  public void process_shouldReturnEmptyProfile_givenNewAccountWithNotIsAssignedToSalesGroups()
      throws Exception {
    SsoLoginProfileRequestDto request = buildRequest();
    String newEmail = "newEmail@bbv.ch";
    request.setEmail(newEmail);
    request.setUserName(newEmail);
    when(axAccountService.searchSaleAccount(newEmail)).thenReturn(Optional.empty());

    when(aadGroupRepo.findAllByUuids(GROUP_UUIDS)).thenReturn(Optional.empty());
    SsoLoginProfileResponseDto response = ssoLoginProfileServiceCaseOne.process(request);
    assertNull(response.getUsername());
  }

  @Test
  public void process_shouldCreateNewSalesAccount_givenNotFoundEmailButGroupIsSalesGroup()
      throws Exception {
    when(axAccountService.searchSaleAccount(EMAIL)).thenReturn(Optional.empty());
    when(aadGroupRepo.findAllByUuids(GROUP_UUIDS))
        .thenReturn(Optional.of(Arrays.asList(aadGroup())));

    mockCreateUserSetting();
    mockCreateEshopUser();
    mockSssignSalesRole();
    ssoLoginProfileServiceCaseOne.process(buildRequest());
    verifyExternalUserCreatedSuccessfully();
  }
}
