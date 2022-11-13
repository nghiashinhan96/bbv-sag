package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponUsageCountValidatorTest {

  @InjectMocks
  private CouponUsageCountValidator validator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shouldBeValid_givenValidData() throws Exception {
    CouponValidationData data = getValidata();
    assertFalse(validator.isInValid(data));
  }

  public static CouponValidationData getValidata() {
    CouponConditions conditions = new CouponConditions();
    conditions.setUsageCount(5);
    conditions.setUsedCount(1);
    CouponValidationData data = CouponValidationData.builder().coupConditions(conditions).build();
    return data;
  }

  @Test
  public void isInValid_shouldBeInvalid_givenInvalidData() throws Exception {
    CouponValidationData data = getInvalidData();
    assertTrue(validator.isInValid(data));
  }

  public static CouponValidationData getInvalidData() {
    CouponConditions conditions = new CouponConditions();
    conditions.setUsageCount(5);
    conditions.setUsedCount(6);
    CouponValidationData data = CouponValidationData.builder().coupConditions(conditions).build();
    return data;
  }
}
