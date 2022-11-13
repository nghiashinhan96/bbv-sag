package com.sagag.services.service.api.impl;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.ExternalOrganisationService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.mdm.api.impl.DvseUserServiceImpl;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.mdm.utils.DvseUserUtils;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
@Ignore
public class DvseBusinessServiceImplTest {

  @Mock
  private EshopUser eshopUser;

  @Mock
  private ExternalOrganisationService externalOrganisationService;

  @Mock
  private UserService userService;

  @Mock
  private DvseUserServiceImpl dvseService;

  @Mock
  private ExternalUserService externalUserService;

  @InjectMocks
  private DvseBusinessServiceImpl dvseBusinessServiceImpl;

  @Mock
  private ExternalOrganisationRepository extOrganisationRepo;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(dvseService, "catalogUri",
        "https://web1.dvse.de/loginh.aspx?SID=435001");
  }

  @Test
  public void testCreateDvseUserInfo_OK()
      throws UserValidationException, MdmCustomerNotFoundException {
    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(extOrganisationRepo.findExternalCustomerIdByOrgIdAndExternalApp(Mockito.any(),
        Mockito.eq(ExternalApp.DVSE))).thenReturn(expectedCustomerId);
    when(dvseService.createUser(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(null);
    when(externalUserService.addExternalUser(Mockito.any())).thenReturn(null);
    when(externalUserService.isUsernameExisted(Mockito.any())).thenReturn(false);
    when(dvseService.existDvseCustomerId(anyString())).thenReturn(true);
    when(userService.getOrgIdByUserId(Mockito.any())).thenReturn(Optional.of(1));

    dvseBusinessServiceImpl.createDvseUserInfo(eshopUser.getId(), SupportedAffiliate.DERENDINGER_AT,
        1);
    verify(externalUserService, times(1)).addExternalUser(Mockito.any());
    verify(externalUserService, times(1)).isUsernameExisted(Mockito.any());
  }

  @Test
  public void testCreateDvseUserInfo_FailedToCreateUniqueUsername()
      throws MdmCustomerNotFoundException {
    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(extOrganisationRepo.findExternalCustomerIdByOrgIdAndExternalApp(Mockito.any(),
        Mockito.any())).thenReturn(expectedCustomerId);
    when(dvseService.createUser(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(null);
    when(externalUserService.addExternalUser(Mockito.any())).thenReturn(null);
    when(externalUserService.isUsernameExisted(Mockito.any())).thenReturn(true);
    when(dvseService.existDvseCustomerId(anyString())).thenReturn(true);
    when(userService.getOrgIdByUserId(Mockito.any())).thenReturn(Optional.of(1));
    try {
      dvseBusinessServiceImpl.createDvseUserInfo(eshopUser.getId(),
          SupportedAffiliate.DERENDINGER_AT, 1);
    } catch (UserValidationException ex) {
      verify(externalUserService, times(DvseUserUtils.MAX_UNIQUE_NAME_RETRY))
          .isUsernameExisted(Mockito.any());
      assertThat(ex.getKey(), Matchers.is(UserErrorCase.UE_MUC_001.key()));
    }
  }

  @Test(expected = MdmCustomerNotFoundException.class)
  public void createDvseUserInfo_shouldThrowException_givenNotFoundExternalCustomerId()
      throws UserValidationException, MdmCustomerNotFoundException {
    final String expectedCustomerId = "846689";
    when(extOrganisationRepo.findExternalCustomerIdByOrgIdAndExternalApp(Mockito.any(),
        Mockito.any())).thenReturn(expectedCustomerId);
    when(dvseService.createUser(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(null);
    when(externalUserService.addExternalUser(Mockito.any())).thenReturn(null);
    when(externalUserService.isUsernameExisted(Mockito.any())).thenReturn(false);
    when(dvseService.existDvseCustomerId(anyString())).thenReturn(false);
    when(userService.getOrgIdByUserId(Mockito.any())).thenReturn(Optional.of(1));
    dvseBusinessServiceImpl.createDvseUserInfo(eshopUser.getId(), SupportedAffiliate.DERENDINGER_AT,
        1);
  }

  @Test
  public void shouldCreateDvseUserInfo_InOrderInvoke()
      throws UserValidationException, MdmCustomerNotFoundException {
    final String expectedCustomerId = "841832";
    when(extOrganisationRepo.findExternalCustomerIdByOrgIdAndExternalApp(Mockito.any(),
        Mockito.any())).thenReturn(expectedCustomerId);

    when(externalUserService.addExternalUser(Mockito.any())).thenReturn(null);
    when(externalUserService.isUsernameExisted(Mockito.any())).thenReturn(false);
    when(dvseService.existDvseCustomerId(expectedCustomerId)).thenReturn(true);
    when(dvseService.createUser(eq(expectedCustomerId), anyString(), anyString(),
        eq(SupportedAffiliate.DERENDINGER_AT))).thenReturn(null);
    when(userService.getOrgIdByUserId(Mockito.any())).thenReturn(Optional.of(1));
    // create an inOrder verifier for a single mock
    InOrder inOrder = inOrder(dvseService);

    dvseBusinessServiceImpl.createDvseUserInfo(eshopUser.getId(), SupportedAffiliate.DERENDINGER_AT,
        1);

    // following will make sure that existDvseCustomerId is first called then createUser is called.
    inOrder.verify(dvseService).existDvseCustomerId(expectedCustomerId);
    inOrder.verify(dvseService).createUser(eq(expectedCustomerId), anyString(), anyString(),
        eq(SupportedAffiliate.DERENDINGER_AT));
  }

}
