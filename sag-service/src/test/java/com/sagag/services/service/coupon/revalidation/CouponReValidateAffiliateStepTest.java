package com.sagag.services.service.coupon.revalidation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponAffiliateValidator;
import com.sagag.services.service.coupon.validator.CouponAffiliateValidatorTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponReValidateAffiliateStepTest {

  @InjectMocks
  private CouponReValidateAffiliateStep couponReValidateAffiliateStep;

  @Mock
  private CouponAffiliateValidator affiliateValidator;

  @Mock
  private CouponCacheService couponCacheService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void handle_shouldClearCacheAndResetDiscount_givenInvalidData() throws Exception {
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(100.0);
    CouponValidationData data = CouponAffiliateValidatorTest.getInvalidData();
    data.setCart(cart);
    when(affiliateValidator.isInValid(data)).thenReturn(true);
    doNothing().when(couponCacheService).clearCache(data.getCouponCode());
    CouponValidationData results = couponReValidateAffiliateStep.handle(data);

    verify(couponCacheService, times(1)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(0.0));
  }

  @Test
  public void handle_shouldNotClearCacheAndResetDiscount_givenValidData() throws Exception {
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);
    CouponValidationData data = CouponAffiliateValidatorTest.getValidData();
    data.setCart(cart);
    when(affiliateValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponReValidateAffiliateStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
  }
}
