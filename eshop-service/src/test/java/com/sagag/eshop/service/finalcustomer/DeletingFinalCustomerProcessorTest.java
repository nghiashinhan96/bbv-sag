package com.sagag.eshop.service.finalcustomer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.FinalCustomerPropertyRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.services.common.enums.FinalCustomerStatus;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DeletingFinalCustomerProcessorTest {

  @InjectMocks
  private DeletingFinalCustomerProcessor deletingFinalCustomerProcessor;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private LoginRepository loginRepo;

  @Mock
  private FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Mock
  private FinalCustomerUserLoginStatusProcessor finalCustomerUserLoginStatusProcessor;

  @Mock
  private CustomerSettingsRepository customerSettingsRepository;

  @Test
  public void process_shouldUpdateCustomerStatus_givenFialCustomerOrgId() throws Exception {
    FinalCustomerProperty status = new FinalCustomerProperty();

    when(finalCustomerPropertyRepo.findByOrgIdAndSettingKey(1L,
        SettingsKeys.FinalCustomer.Settings.STATUS.name())).thenReturn(Optional.of(status));
    mockDeactivateUsers();

    when(customerSettingsRepository.findSettingsByOrgId(Mockito.anyInt()))
    .thenReturn(new CustomerSettings());

    deletingFinalCustomerProcessor.process(1);
    assertThat(status.getValue(), Matchers.is(FinalCustomerStatus.DELETED.name()));
  }

  @Test
  public void process_shouldDeactivateUsers_givenFialCustomerOrgId() throws Exception {
    Login login = new Login();
    EshopUser user = new EshopUser();
    user.setLogin(login);

    mockChangeCustomerStatus();

    when(customerSettingsRepository.findSettingsByOrgId(Mockito.anyInt()))
        .thenReturn(new CustomerSettings());

    deletingFinalCustomerProcessor.process(1);
    assertFalse(login.isUserActive());
  }

  private void mockChangeCustomerStatus() {
    when(finalCustomerPropertyRepo.findByOrgIdAndSettingKey(1L,
        SettingsKeys.FinalCustomer.Settings.STATUS.name())).thenReturn(Optional.empty());
  }

  private void mockDeactivateUsers() {
    Mockito.doNothing().when(finalCustomerUserLoginStatusProcessor).process(1, false);
  }
}
