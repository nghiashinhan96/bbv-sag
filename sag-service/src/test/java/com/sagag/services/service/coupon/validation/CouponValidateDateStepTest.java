package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponDateValidator;
import com.sagag.services.service.coupon.validator.CouponDateValidatorTest;
import com.sagag.services.service.exception.coupon.ExpiredCouponException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateDateStepTest {

  @InjectMocks
  private CouponValidateDateStep couponValidateDateStep;

  @Mock
  private CouponDateValidator dateValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = ExpiredCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponDateValidatorTest.getInvalidData();
    when(dateValidator.isInValid(data)).thenReturn(true);
    couponValidateDateStep.handle(data);
  }

  @Test
  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    CouponValidationData data = CouponDateValidatorTest.getValidData();
    when(dateValidator.isInValid(data)).thenReturn(false);
    assertNotNull(couponValidateDateStep.handle(data));
  }
}
