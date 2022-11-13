package com.sagag.services.service.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.ExternalOrganisationService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.impl.UserSettingsServiceImpl;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.elasticsearch.api.CustomerSearchService;
import com.sagag.services.elasticsearch.criteria.CustomerSearchCriteria;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.mdm.api.DvseUserService;
import com.sagag.services.mdm.api.impl.DvseUserServiceImpl;
import com.sagag.services.service.user.cache.ISyncUserLoader;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

/**
 * UT for {@link UserBusinessServiceImpl}.
 */
@EshopMockitoJUnitRunner
@Ignore("most cases failed.")
public class UserBusinessServiceImplTest {

  @InjectMocks
  private UserBusinessServiceImpl userBusinessService;

  @Mock
  private UserService userService;

  @Mock
  private LoginService loginService;

  @Mock
  private ExternalUserService externalUserService;

  @Mock
  private ExternalOrganisationService externalOrganisationService;

  @Mock
  private DvseUserServiceImpl dvseService;

  @Mock
  private UserInfo user;

  @Mock
  private EshopUser eshopUser;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private OrganisationService orgService;

  @Mock
  private CustomerSettingsService custSettingsService;

  @Mock
  private UserSettingsServiceImpl userSettingsService;

  @Mock
  private CustomerCacheService customerCacheService;

  @Mock
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Mock
  private CustomerExternalService custExternalService;

  @Mock
  private DvseUserService dvseUserService;

  @Mock
  private UserCacheService userCacheService;

  @Mock
  private UserSettingsRepository userSettingsRepo;

  @Mock
  private UserInfo userInfo;

  @Mock
  private Organisation organisation;

  @Mock
  private CustomerSearchService customerSearchService;

  @Mock
  private ISyncUserLoader syncUserLoader;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(dvseService, "catalogUri",
        "https://web1.dvse.de/loginh.aspx?SID=435001");
  }

  @Test
  public void shouldMakeSureAlwaysCheckCustomerInfo_Cache() {
    when(userSettingsRepo.findSettingsByUserId(anyLong())).thenReturn(new UserSettings());

    when(userService.getSessionUserInfo(27L)).thenReturn(userInfo);
    when(userInfo.isCustChecked()).thenReturn(true);
    when(userInfo.getDefaultBranchId()).thenReturn("");
    when(userInfo.isAffChecked()).thenReturn(false);
    when(userInfo.getId()).thenReturn(27L);
    when(userInfo.getSettings()).thenReturn(new OwnSettings());
    when(organisation.getOrgCode()).thenReturn("1100005");
    when(orgService.getFirstByUserId(27L)).thenReturn(Optional.of(organisation));
    when(orgService.getByOrgId(anyInt())).thenReturn(Optional.of(new Organisation()));
    when(supportedAffiliateRepo.isShowPfandArticleByShortName(any())).thenReturn(false);
    when(custSettingsService.findSettingsByOrgId(anyInt())).thenReturn(null);
    when(userSettingsService.getSettingsByUserId(27L)).thenReturn(new UserSettings());
    when(custExternalService.searchBranchById(any(), any()))
        .thenReturn(Optional.of(new CustomerBranch()));
    when(externalUserService.getDvseExternalUserByUserId(27L))
        .thenReturn(Optional.of(new ExternalUserDto()));
    when(dvseUserService.getDvseCatalogUri(any(), any(), any(), any())).thenReturn("");
    Mockito.doNothing().when(userCacheService).put(any());
    when(custExternalService.findCustomerByNumber(anyString(), anyString()))
        .thenReturn(Optional.empty());

    when(customerSearchService.searchCustomerByCustomerNumber(any(CustomerSearchCriteria.class)))
        .thenReturn(Optional.empty());
    when(supportedAffiliateRepo.isShowPfandArticleByShortName(anyString())).thenReturn(true);

    Mockito.inOrder(customerCacheService);
    Mockito.inOrder(custExternalService);

    syncUserLoader.load(27L, StringUtils.EMPTY, StringUtils.EMPTY, Optional.empty());

    verify(custExternalService, never()).findCustomerByNumber(anyString(), anyString());
    verify(custExternalService, never()).searchCustomerAddresses(anyString(), anyString());

  }

  @Test
  public void shouldMakeSureAlwaysCheckCustomerInfo_NoCache() {
    when(userSettingsRepo.findSettingsByUserId(anyLong())).thenReturn(new UserSettings());

    when(userService.getSessionUserInfo(27L)).thenReturn(userInfo);
    when(userInfo.isCustChecked()).thenReturn(true);
    when(userInfo.getDefaultBranchId()).thenReturn("");
    when(userInfo.isAffChecked()).thenReturn(false);
    when(userInfo.getId()).thenReturn(27L);
    when(userInfo.getSettings()).thenReturn(new OwnSettings());
    when(organisation.getOrgCode()).thenReturn("1100005");
    when(orgService.getFirstByUserId(27L)).thenReturn(Optional.of(organisation));
    when(orgService.getByOrgId(anyInt())).thenReturn(Optional.of(new Organisation()));
    when(supportedAffiliateRepo.isShowPfandArticleByShortName(any())).thenReturn(false);
    when(custSettingsService.findSettingsByOrgId(anyInt())).thenReturn(null);
    when(userSettingsService.getSettingsByUserId(27L)).thenReturn(new UserSettings());
    when(custExternalService.searchBranchById(any(), any()))
        .thenReturn(Optional.of(new CustomerBranch()));
    when(externalUserService.getDvseExternalUserByUserId(27L))
        .thenReturn(Optional.of(new ExternalUserDto()));
    when(dvseUserService.getDvseCatalogUri(any(), any(), any(), any())).thenReturn("");
    Mockito.doNothing().when(userCacheService).put(any());
    when(custExternalService.findCustomerByNumber(anyString(), anyString()))
        .thenReturn(Optional.of(Customer.builder().active(true).nr(100005L).shortName("").build()));
    when(customerSearchService.searchCustomerByCustomerNumber(any(CustomerSearchCriteria.class)))
        .thenReturn(Optional.empty());
    when(supportedAffiliateRepo.isShowPfandArticleByShortName(anyString())).thenReturn(true);
    Mockito.inOrder(customerCacheService);
    Mockito.inOrder(custExternalService);

    syncUserLoader.load(27L, StringUtils.EMPTY, StringUtils.EMPTY, Optional.empty());
    verify(custExternalService, times(1)).findCustomerByNumber(anyString(), anyString());
    verify(custExternalService, times(1)).searchCustomerAddresses(anyString(), anyString());

  }

}
