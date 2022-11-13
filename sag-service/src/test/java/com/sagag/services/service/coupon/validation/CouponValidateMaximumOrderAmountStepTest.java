package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponMaximumOrderAmountValidator;
import com.sagag.services.service.coupon.validator.CouponMaximumOrderAmountValidatorTest;
import com.sagag.services.service.exception.coupon.MaximumTotalCouponException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateMaximumOrderAmountStepTest {

  @InjectMocks
  private CouponValidateMaximumOrderAmountStep couponValidateMaximumOrderAmountStep;

  @Mock
  private CouponMaximumOrderAmountValidator maximumOrderAmountValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = MaximumTotalCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponMaximumOrderAmountValidatorTest.getInvalidData();
    when(maximumOrderAmountValidator.isInValid(data)).thenReturn(true);
    couponValidateMaximumOrderAmountStep.handle(data);
  }

  @Test
  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    CouponValidationData data = CouponMaximumOrderAmountValidatorTest.getValidData();
    when(maximumOrderAmountValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponValidateMaximumOrderAmountStep.handle(data);
    assertNotNull(results);
  }
}
