package com.sagag.eshop.service.validator.vat.display;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class VatTypeDisplayValidatorTest {

  @InjectMocks
  private VatTypeDisplayValidator validator;

  @Test
  public void validate_shouldReturnTrue_1() {
    String vatDisplayValue = "11";
    Boolean result = validator.validate(vatDisplayValue);
    assertTrue(result);
  }
  
  @Test
  public void validate_shouldReturnTrue_2() {
    String vatDisplayValue = "01";
    Boolean result = validator.validate(vatDisplayValue);
    assertTrue(result);
  }


  @Test
  public void validate_shouldReturnFalse_wrongFormat_1() throws Exception {
    String vatDisplayValue = "1";
    Boolean result = validator.validate(vatDisplayValue);
    assertFalse(result);
  }
  
  @Test
  public void validate_shouldReturnFalse_wrongFormat_2() throws Exception {
    String vatDisplayValue = "111";
    Boolean result = validator.validate(vatDisplayValue);
    assertFalse(result);
  }
  
  
  @Test
  public void validate_shouldReturnFalse_wrongFormat_3() throws Exception {
    String vatDisplayValue = "23";
    Boolean result = validator.validate(vatDisplayValue);
    assertFalse(result);
  }
}
