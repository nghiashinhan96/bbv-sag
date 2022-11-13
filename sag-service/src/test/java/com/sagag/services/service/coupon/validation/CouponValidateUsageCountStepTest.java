package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponUsageCountValidator;
import com.sagag.services.service.coupon.validator.CouponUsageCountValidatorTest;
import com.sagag.services.service.exception.coupon.FullUsageCouponException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@EshopMockitoJUnitRunner
public class CouponValidateUsageCountStepTest {

  @InjectMocks
  private CouponValidateUsageCountStep couponValidateUsageCountStep;

  @Mock
  private CouponUsageCountValidator usageCountValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = FullUsageCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponUsageCountValidatorTest.getInvalidData();
    when(usageCountValidator.isInValid(data)).thenReturn(true);
    couponValidateUsageCountStep.handle(data);
  }

  public void handle_shouldReturnDataWithRemainUsage_givenValidData() throws Exception {
    CouponValidationData data = CouponUsageCountValidatorTest.getValidata();
    when(usageCountValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponValidateUsageCountStep.handle(data);
    assertThat(results.getRemainUsage(), Matchers.is(4));
  }
}

