package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponLimitUsedValidator;
import com.sagag.services.service.coupon.validator.CouponLimitUsedValidatorTest;
import com.sagag.services.service.exception.coupon.LimitUsedCouponException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateLimitUsedStepTest {

  @InjectMocks
  private CouponValidateLimitUsedStep couponValidateLimitUsedStep;

  @Mock
  private CouponLimitUsedValidator limitUsedValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = LimitUsedCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponLimitUsedValidatorTest.getData();
    when(limitUsedValidator.isInValid(data)).thenReturn(true);
    couponValidateLimitUsedStep.handle(data);
  }

  @Test
  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    CouponValidationData data = CouponLimitUsedValidatorTest.getData();
    when(limitUsedValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponValidateLimitUsedStep.handle(data);
    assertNotNull(results);
  }
}
