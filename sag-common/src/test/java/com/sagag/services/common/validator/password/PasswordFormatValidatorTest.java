package com.sagag.services.common.validator.password;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PasswordFormatValidatorTest {

  @InjectMocks
  private PasswordFormatValidator validator;

  @Test
  public void testValidatePassword_Success() {
    String pass = "123asd@#";
    Assert.assertTrue(validator.validate(pass));
  }

  @Test
  public void testValidatePassword_Success2() {
    String pass = "a1#$£/! @-.";
    Assert.assertTrue(validator.validate(pass));
  }

  @Test
  public void testValidatePassword_Fails() {
    String pass = "123asd@#&";
    Assert.assertFalse(validator.validate(pass));
  }

  @Test
  public void testValidatePassword_Fails2() {
    String pass = "1231223344";
    Assert.assertFalse(validator.validate(pass));
  }
}
