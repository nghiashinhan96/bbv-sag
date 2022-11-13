package com.sagag.eshop.service.api.sso;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileResponseDto;
import com.sagag.eshop.service.sso.SsoLoginProfileServiceCaseTwo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SsoLoginProfileServiceCaseTwoTest extends AbstractSsoLoginProfileServiceTest {

  @InjectMocks
  protected SsoLoginProfileServiceCaseTwo ssoLoginProfileServiceCaseTwo;

  @Test
  public void process_shouldReturnEmptyProfile_givenAccountNotBelongToAnyGroup() throws Exception {
    when(aadGroupRepo.findAllByUuids(GROUP_UUIDS)).thenReturn(Optional.empty());
    SsoLoginProfileResponseDto response = ssoLoginProfileServiceCaseTwo.process(buildRequest());
    assertNull(response.getUsername());
  }

  @Test
  public void process_updateEmail_givenMatchUuidAccount() throws Exception {
    SsoLoginProfileRequestDto request = buildRequest();
    String newEmail = "newEmail@bbv.ch";
    request.setEmail(newEmail);
    request.setUserName(newEmail);

    when(aadGroupRepo.findAllByUuids(GROUP_UUIDS))
        .thenReturn(Optional.of(Arrays.asList(aadGroup())));

    AadAccounts aadAccounts = mockAadAccount();
    when(aadAccountsRepo.findByUuid(UUID)).thenReturn(Optional.of(aadAccounts));

    ssoLoginProfileServiceCaseTwo.process(request);
    verify(aadAccountsService, times(1)).updateEmail(aadAccounts, newEmail);
  }

  @Test
  public void process_shouldCreateNewSalesAccounts_givenNotFoundSalesAccount() throws Exception {

    SsoLoginProfileRequestDto request = buildRequest();
    when(aadGroupRepo.findAllByUuids(GROUP_UUIDS))
        .thenReturn(Optional.of(Arrays.asList(aadGroup())));
    when(aadAccountsRepo.findByUuid(UUID)).thenReturn(Optional.empty());

    when(axAccountService.searchSaleAccount(EMAIL)).thenReturn(Optional.empty());
    mockCreateUserSetting();
    mockCreateEshopUser();
    mockSssignSalesRole();
    ssoLoginProfileServiceCaseTwo.process(request);
    verify(aadAccountsRepo, times(1)).save(Mockito.any(AadAccounts.class));
    verifyExternalUserCreatedSuccessfully();
  }
}
