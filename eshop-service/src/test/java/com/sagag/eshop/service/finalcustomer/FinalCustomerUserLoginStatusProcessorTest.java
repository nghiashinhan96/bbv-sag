package com.sagag.eshop.service.finalcustomer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class FinalCustomerUserLoginStatusProcessorTest {

  @InjectMocks
  private FinalCustomerUserLoginStatusProcessor finalCustomerUserLoginStatusProcessor;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private LoginRepository loginRepo;

  @Test
  public void process_shouldActivateUser_givenOrgIdAndStatus() throws Exception {
    Login login = new Login();
    EshopUser user = new EshopUser();
    user.setLogin(login);

    Mockito.when(eshopUserRepo.findUsersByOrgId(1)).thenReturn(Arrays.asList(user));
    finalCustomerUserLoginStatusProcessor.process(1, true);
    assertTrue(login.isUserActive());
  }

  @Test
  public void process_shouldDeactivateUser_givenOrgIdAndStatus() throws Exception {
    Login login = new Login();
    EshopUser user = new EshopUser();
    user.setLogin(login);

    Mockito.when(eshopUserRepo.findUsersByOrgId(1)).thenReturn(Arrays.asList(user));
    finalCustomerUserLoginStatusProcessor.process(1, false);
    assertFalse(login.isUserActive());
  }
}
