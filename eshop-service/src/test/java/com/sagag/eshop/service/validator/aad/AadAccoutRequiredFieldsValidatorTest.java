package com.sagag.eshop.service.validator.aad;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.services.domain.eshop.dto.AadAccountsDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AadAccoutRequiredFieldsValidatorTest {

  @InjectMocks
  private AadAccoutRequiredFieldsValidator validator;

  @Test
  public void validate_shouldReturnTrue_givenValidData() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().personalNumber("123").firstName("jon")
        .lastName("hon min").primaryContactEmail("jonhomin@bbv.ch").build();
    boolean result = validator.validate(dto);
    assertTrue(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validate_shouldReturnFalse_givenDataMissingPersonalNr() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().firstName("jon").lastName("hon min")
        .primaryContactEmail("jonhomin@bbv.ch").build();
    boolean result = validator.validate(dto);
    assertFalse(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validate_shouldReturnFalse_givenDataMissingFirstName() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().personalNumber("123").lastName("hon min")
        .primaryContactEmail("jonhomin@bbv.ch").build();
    boolean result = validator.validate(dto);
    assertFalse(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validate_shouldReturnFalse_givenDataMissingLastName() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().personalNumber("123").firstName("jon")
        .primaryContactEmail("jonhomin@bbv.ch").build();
    boolean result = validator.validate(dto);
    assertFalse(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validate_shouldReturnFalse_givenDataMissingEmail() throws Exception {
    AadAccountsDto dto =
        AadAccountsDto.builder().personalNumber("123").firstName("jon").lastName("hon min").build();
    boolean result = validator.validate(dto);
    assertFalse(result);
  }
}
