package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.hazelcast.api.CustomerCacheService;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * UT for {@link CustomerBusinessServiceImpl}.
 */
@EshopMockitoJUnitRunner

public class CustomerBusinessServiceImplTest {

  @InjectMocks
  private CustomerBusinessServiceImpl customerBusinessService;

  @Mock
  private CustomerExternalService custExternalService;

  @Mock
  private OrganisationService organisationService;

  @Mock
  private UserSettingsRepository userSettingsRepository;

  @Mock
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Mock
  private CustomerCacheService customerCacheService;

  @Mock
  private UserService userService;

  /*
   * @Before public void setup() {
   * when(organisationService.findOrganisationByShortname(ArgumentMatchers.anyString()))
   * .thenReturn(Optional.empty());
   *
   * final List<OrganisationSettings> organisationSettings = new ArrayList<>(); final
   * OrganisationSettings additionalCreditSetting = OrganisationSettings.builder()
   * .settingKey("payment_additional_credit_direct_invoice").settingValue("1000").build();
   * organisationSettings.add(additionalCreditSetting); final Organisation organisationSetting =
   * Organisation.builder().organisationSettings(organisationSettings).build();
   * when(organisationService.findOrganisationByShortname("derendinger-at"))
   * .thenReturn(Optional.of(organisationSetting));
   *
   * when(userSettingsRepository.findByUserId(ArgumentMatchers.anyLong()))
   * .thenReturn(Optional.empty());
   *
   * when(organisationSettingRepository.findByOrganisationShortnameAndSettingKey(
   * ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.empty());
   * when(organisationSettingRepository.findByOrganisationShortnameAndSettingKey("derendinger-at",
   * "payment_additional_credit_direct_invoice")) .thenReturn(Optional.of(additionalCreditSetting));
   * }
   */

  @Test
  public void test() {
    // We need do them again.
  }

}
