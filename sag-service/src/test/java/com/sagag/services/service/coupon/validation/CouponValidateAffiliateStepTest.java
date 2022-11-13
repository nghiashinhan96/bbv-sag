package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponAffiliateValidator;
import com.sagag.services.service.coupon.validator.CouponAffiliateValidatorTest;
import com.sagag.services.service.exception.coupon.InvalidCustomerCouponException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateAffiliateStepTest {

  @InjectMocks
  private CouponValidateAffiliateStep couponValidateAffiliateStep;

  @Mock
  private CouponAffiliateValidator affiliateValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = InvalidCustomerCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponAffiliateValidatorTest.getInvalidData();
    when(affiliateValidator.isInValid(data)).thenReturn(true);
    couponValidateAffiliateStep.handle(data);
  }

  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    CouponValidationData data = CouponAffiliateValidatorTest.getInvalidData();
    when(affiliateValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponValidateAffiliateStep.handle(data);
    assertNotNull(results);
  }
}
