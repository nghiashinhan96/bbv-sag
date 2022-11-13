package com.sagag.services.oauth2.api.impl;

import static org.mockito.ArgumentMatchers.anyString;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.oauth2.api.DataProvider;
import com.sagag.services.oauth2.exception.BlockedCustomerException;
import com.sagag.services.oauth2.settings.CustomerSettingsProcessor;
import com.sagag.services.oauth2.settings.UpdatedCustomerSettingsDto;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CustomerSettingsProcessorTest {

  @InjectMocks
  private CustomerSettingsProcessor customerSettingsProcessor;

  @Mock
  private CustomerSettingsRepository customerSettingsRepo;

  @Mock
  private PaymentMethodRepository paymentRepo;

  @Mock
  private DeliveryTypesRepository deliveryTypesRepo;

  @Mock
  private CustomerCacheService customerCacheService;

  @Mock
  private Customer customer;

  @Mock
  private CustomerSettingsService customerSettingsService;

  @Before
  public void setUp() {
    Mockito.when(customerCacheService.getCachedCustomer(anyString(), anyString()))
    .thenThrow(BlockedCustomerException.class);
  }

  @Test(expected = BlockedCustomerException.class)
  public void givenUserWhenAXCustomerInactiveThenThrowUserDeniedAuthorizationException() {
    String username = "nu1.ga";
    EshopUser eshopUser =
        DataProvider.buildActiveEshopUser(26L, username, EshopAuthority.SALES_ASSISTANT.name());

    Optional<UpdatedCustomerSettingsDto> customerSettings = customerSettingsProcessor.process(eshopUser);
    Assert.assertThat(customerSettings.isPresent(), Is.is(true));
  }

  @Test(expected = BlockedCustomerException.class)
  public void givenUserWhenPaymentMethodFailedThenThrowIllegalArgumentException() {
    testProcessException();
  }

  private void testProcessException() {
    String username = "nu1.ga";
    EshopUser eshopUser =
        DataProvider.buildActiveEshopUser(26L, username, EshopAuthority.SALES_ASSISTANT.name());

    customerSettingsProcessor.process(eshopUser);
  }

}
