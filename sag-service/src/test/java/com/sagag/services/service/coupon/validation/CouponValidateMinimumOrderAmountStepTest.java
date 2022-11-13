package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponMinimumOrderAmountValidator;
import com.sagag.services.service.coupon.validator.CouponMinimumOrderAmountValidatorTest;
import com.sagag.services.service.exception.coupon.MinimumTotalCouponException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateMinimumOrderAmountStepTest {

  @InjectMocks
  private CouponValidateMinimumOrderAmountStep couponValidateMinimumOrderAmountStep;

  @Mock
  private CouponMinimumOrderAmountValidator minimumOrderAmountValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = MinimumTotalCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponMinimumOrderAmountValidatorTest.getInvalidData();
    when(minimumOrderAmountValidator.isInValid(data)).thenReturn(true);
    couponValidateMinimumOrderAmountStep.handle(data);
  }

  @Test
  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    CouponValidationData data = CouponMinimumOrderAmountValidatorTest.getValidData();
    when(minimumOrderAmountValidator.isInValid(data)).thenReturn(false);
    CouponValidationData result = couponValidateMinimumOrderAmountStep.handle(data);
    assertNotNull(result);
  }
}
