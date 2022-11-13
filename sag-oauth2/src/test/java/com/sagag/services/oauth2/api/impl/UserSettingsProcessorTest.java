package com.sagag.services.oauth2.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.google.common.collect.ImmutableList;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.oauth2.api.DataProvider;
import com.sagag.services.oauth2.settings.CustomerSettingsProcessor;
import com.sagag.services.oauth2.settings.UpdatedCustomerSettingsDto;
import com.sagag.services.oauth2.settings.UserSettingsProcessor;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsProcessorTest {

  @InjectMocks
  private UserSettingsProcessor userSettingsProcessor;

  @Mock
  private CustomerSettingsProcessor customerSettingsProcessor;

  @Mock
  private CustomerCacheService customerCacheService;

  @Mock
  private UserSettingsService userSettingsService;

  @Mock
  private Customer customer;

  @Mock
  private UserSettingsRepository userSettingsRepo;

  @Mock
  private AddressFilterService addressFilterService;

  @Before
  public void init() {
    UpdatedCustomerSettingsDto customerSettings = DataProvider.buildCustomerSettings();
    customerSettings.setErpCustomer(customer);
    given(customerSettingsProcessor.process(any(EshopUser.class)))
        .willReturn(Optional.ofNullable(customerSettings));
    given(userSettingsRepo.save(any(UserSettings.class))).willReturn(new UserSettings());
  }

  @Test
  public void given_FoundUser_thenProcessOneAddressReturn() {
    String username = "nu1.ga";
    Address address1 = new Address();
    address1.setId("1");
    List<Address> addresses = ImmutableList.of(address1);
    given(customer.getAffiliateShortName()).willReturn(SupportedAffiliate.TECHNOMAG.getAffiliate());
    given(customerCacheService.getCachedCustomerAddresses(any(String.class), anyString()))
        .willReturn(addresses);
    EshopUser eshopUser =
        DataProvider.buildActiveEshopUser(26L, username, EshopAuthority.SALES_ASSISTANT.name());

    UserSettings userSettings = userSettingsProcessor.process(eshopUser).get();

    Assert.assertThat("user setting id is incorrect", userSettings.getId(), Is.is(1));
  }

  @Test
  public void given_FoundUser_thenProcessMoreThanOneAddressReturn() {
    String username = "nu1.ga";
    Address address1 = new Address();
    address1.setId("1");
    Address address2 = new Address();
    address2.setId("2");
    List<Address> addresses = ImmutableList.of(address1, address2);
    given(customer.getAffiliateShortName()).willReturn(SupportedAffiliate.TECHNOMAG.getAffiliate());
    given(customerCacheService.getCachedCustomerAddresses(any(String.class), anyString()))
        .willReturn(addresses);

    EshopUser eshopUser =
        DataProvider.buildActiveEshopUser(26L, username, EshopAuthority.SALES_ASSISTANT.name());

    UserSettings userSettings = userSettingsProcessor.process(eshopUser).get();

    Assert.assertThat("user setting id is incorrect", userSettings.getId(), Is.is(1));
  }

}
