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
import com.sagag.services.service.coupon.validator.CouponUsageCountValidator;
import com.sagag.services.service.coupon.validator.CouponUsageCountValidatorTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponReValidateUsageCountStepTest {

  private static final String COUPON_CODE = "CP01";

  @InjectMocks
  private CouponReValidateUsageCountStep couponReValidateUsageCountStep;

  @Mock
  private CouponUsageCountValidator usageCountValidator;

  @Mock
  private CouponCacheService couponCacheService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void handle_shouldClearCacheAndResetDiscount_givenInvalidData() throws Exception {
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);
    CouponConditions conditions = new CouponConditions();
    conditions.setCouponsCode(COUPON_CODE);
    CouponValidationData data = CouponUsageCountValidatorTest.getInvalidData();
    data.setCoupConditions(conditions);
    data.setCart(cart);

    when(usageCountValidator.isInValid(data)).thenReturn(true);
    doNothing().when(couponCacheService).clearCache(COUPON_CODE);
    CouponValidationData results = couponReValidateUsageCountStep.handle(data);

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
    CouponValidationData data = CouponUsageCountValidatorTest.getInvalidData();
    data.setCoupConditions(conditions);
    data.setCart(cart);

    when(usageCountValidator.isInValid(data)).thenReturn(false);
    doNothing().when(couponCacheService).clearCache(COUPON_CODE);
    CouponValidationData results = couponReValidateUsageCountStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
  }
}
