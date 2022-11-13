package com.sagag.services.ivds.validator;

import com.sagag.services.ivds.DataProvider;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlateNumberValidatorTest {

  @InjectMocks
  private PlateNumberValidator validator;

  @Test
  public void verifyPlateNumber() {
    Assert.assertThat(validator.validate(StringUtils.EMPTY), Matchers.is(false));
    Assert.assertThat(validator.validate(DataProvider.PLATE_SZ104289), Matchers.is(true));
  }

}
