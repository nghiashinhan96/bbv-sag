package com.sagag.services.service.coupon.revalidation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponLimitUsedValidator;
import com.sagag.services.service.coupon.validator.CouponLimitUsedValidatorTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponReValidateLimitUsedStepTest {

  private static final String COUPON_CODE = "CP01";

  @InjectMocks
  private CouponReValidateLimitUsedStep couponReValidateLimitUsedStep;

  @Mock
  private CouponLimitUsedValidator limitUsedValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Mock
  private CouponCacheService couponCacheService;

  @Test
  public void handle_shouldClearCacheAndResetDiscount_givenInvalidData() throws Exception {
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);
    CouponConditions conditions = new CouponConditions();
    conditions.setCouponsCode(COUPON_CODE);
    CouponValidationData data = CouponLimitUsedValidatorTest.getData();
    data.setCoupConditions(conditions);
    data.setCart(cart);

    when(limitUsedValidator.isInValid(data)).thenReturn(true);
    doNothing().when(couponCacheService).clearCache(COUPON_CODE);
    CouponValidationData results = couponReValidateLimitUsedStep.handle(data);

    verify(couponCacheService, times(1)).clearCache(COUPON_CODE);
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(0.0));
  }

  @Test
  public void handle_shouldNotClearCacheAndResetDiscount_givenValidData() throws Exception {
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);
    CouponConditions conditions = new CouponConditions();
    conditions.setCouponsCode(COUPON_CODE);
    CouponValidationData data = CouponLimitUsedValidatorTest.getData();
    data.setCoupConditions(conditions);
    data.setCart(cart);

    when(limitUsedValidator.isInValid(data)).thenReturn(false);
    doNothing().when(couponCacheService).clearCache(COUPON_CODE);
    CouponValidationData results = couponReValidateLimitUsedStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
  }
}
