package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponCodeValidator;
import com.sagag.services.service.exception.coupon.NotFoundCouponException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponValidateCodeStepTest {

  private static final String COUPON_CODE = "CP01";

  @InjectMocks
  private CouponValidateCodeStep couponValidateCodeStep;

  @Mock
  private CouponCodeValidator codeValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = NotFoundCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponValidationData.builder().couponCode(COUPON_CODE).build();
    when(codeValidator.isInValid(data)).thenReturn(true);
    couponValidateCodeStep.handle(data);
  }

  @Test(expected = NotFoundCouponException.class)
  public void handle_shouldReturnOriginalData_givenValidData() throws Exception {
    CouponValidationData data = CouponValidationData.builder().couponCode(COUPON_CODE).build();
    when(codeValidator.isInValid(data)).thenReturn(true);
    CouponValidationData stepResult = couponValidateCodeStep.handle(data);
    assertNotNull(stepResult);
    assertThat(stepResult.getCouponCode(), Matchers.is(COUPON_CODE));
  }
}
