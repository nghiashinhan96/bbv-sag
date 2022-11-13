package com.sagag.eshop.service.validator.aad;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.service.api.AxAccountService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AadAccoutDuplicatedEmailValidatorTest {

  @InjectMocks
  private AadAccoutDuplicatedEmailValidator validator;

  @Mock
  private AxAccountService axAccountService;

  @Test
  public void validate_shouldReturnFalse_UniqueEmail() throws Exception {
    String email = "test01@bbv.ch";
    when(axAccountService.searchSaleAccount(email)).thenReturn(Optional.empty());
    boolean result = validator.validate(email);
    assertTrue(result);
  }

  @Test
  public void validate_shouldReturnFalse_givenDuplicatedEmail() throws Exception {
    String email = "test01@bbv.ch";
    AadAccounts account = new AadAccounts();
    account.setPrimaryContactEmail(email);
    when(axAccountService.searchSaleAccount(email)).thenReturn(Optional.of(account));
    boolean result = validator.validate(email);
    assertFalse(result);
  }
}
