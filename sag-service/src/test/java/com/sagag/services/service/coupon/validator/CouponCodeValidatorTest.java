package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.CouponConditionsRepository;
import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

@EshopMockitoJUnitRunner
public class CouponCodeValidatorTest {

  private static final String COUPON_CODE = "CP01";

  @InjectMocks
  private CouponCodeValidator couponCodeValidator;

  @Mock
  private CouponConditionsRepository couponConditionsRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shouldIsInvalid_givenNotFoundCoupon() throws Exception {
    CouponValidationData data = CouponValidationData.builder().couponCode(COUPON_CODE).build();
    when(couponConditionsRepo.findOneByCouponsCode(data.getCouponCode()))
        .thenReturn(Optional.ofNullable(null));
    assertTrue(couponCodeValidator.isInValid(data));
  }

  @Test
  public void isInValid_shouldIsvalid_givenFoundCoupon() throws Exception {
    CouponValidationData data = CouponValidationData.builder().couponCode(COUPON_CODE).build();
    CouponConditions conditions = new CouponConditions();
    conditions.setCouponsCode(COUPON_CODE);
    when(couponConditionsRepo.findOneByCouponsCode(data.getCouponCode()))
        .thenReturn(Optional.of(conditions));
    assertFalse(couponCodeValidator.isInValid(data));
  }
}
