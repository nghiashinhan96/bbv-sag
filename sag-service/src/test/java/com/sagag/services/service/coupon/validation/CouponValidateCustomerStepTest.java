package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponCustomerValidator;
import com.sagag.services.service.coupon.validator.CouponCustomerValidatorTest;
import com.sagag.services.service.exception.coupon.InvalidCustomerCouponException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateCustomerStepTest {

  @InjectMocks
  private CouponValidateCustomerStep couponValidateCustomerStep;

  @Mock
  private CouponCustomerValidator customerValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = InvalidCustomerCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponCustomerValidatorTest.getInvalidData();
    when(customerValidator.isInValid(data)).thenReturn(true);
    couponValidateCustomerStep.handle(data);
  }

  @Test
  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    CouponValidationData data = CouponCustomerValidatorTest.getValidData();
    when(customerValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponValidateCustomerStep.handle(data);
    assertNotNull(results);
  }
}
