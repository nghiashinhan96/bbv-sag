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
import com.sagag.services.service.coupon.validator.CouponCustomerValidator;
import com.sagag.services.service.coupon.validator.CouponCustomerValidatorTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponReValidateCustomerStepTest {

  private static final String CPO1 = "CPO1";

  @InjectMocks
  private CouponReValidateCustomerStep couponReValidateCustomerStep;

  @Mock
  private CouponCustomerValidator customerValidator;

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
    conditions.setCouponsCode(CPO1);
    CouponValidationData data = CouponCustomerValidatorTest.getInvalidData();
    data.setCart(cart);
    data.setCoupConditions(conditions);
    when(customerValidator.isInValid(data)).thenReturn(true);
    CouponValidationData results = couponReValidateCustomerStep.handle(data);

    verify(couponCacheService, times(1)).clearCache(CPO1);
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(0.0));
  }

  @Test
  public void handle_shouldNotClearCacheAndResetDiscount_givenValidData() throws Exception {
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);
    CouponConditions conditions = new CouponConditions();
    conditions.setCouponsCode(CPO1);
    CouponValidationData data = CouponCustomerValidatorTest.getValidData();
    data.setCart(cart);
    data.setCoupConditions(conditions);

    when(customerValidator.isInValid(data)).thenReturn(false);
    doNothing().when(couponCacheService).clearCache(CPO1);
    CouponValidationData results = couponReValidateCustomerStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
  }
}
