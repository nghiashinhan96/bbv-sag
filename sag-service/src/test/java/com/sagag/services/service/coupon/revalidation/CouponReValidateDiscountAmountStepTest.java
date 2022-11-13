package com.sagag.services.service.coupon.revalidation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponDiscountAmountValidator;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@EshopMockitoJUnitRunner
public class CouponReValidateDiscountAmountStepTest {

  private static final String CPO1 = "CPO1";


  @InjectMocks
  private CouponReValidateDiscountAmountStep couponReValidateDiscountAmountStep;

  @Mock
  private CouponDiscountAmountValidator discountAmountValidator;

  @Mock
  private CouponCacheService couponCacheService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void handle_shouldClearCacheAndResetDiscount_givenInvalidData() throws Exception {
    Customer customer = Customer.builder().nr(1100005).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setCompanyName("company_A");
    CouponConditions conditions = new CouponConditions();
    conditions.setDiscountArticleId(1001388532);
    conditions.setCouponsCode(CPO1);
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);

    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).cart(cart).build();
    when(discountAmountValidator.isInValid(data)).thenReturn(true);
    doNothing().when(couponCacheService).clearCache(CPO1);
    CouponValidationData results = couponReValidateDiscountAmountStep.handle(data);

    verify(couponCacheService, times(1)).clearCache(CPO1);
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(0.0));
  }

  @Test
  public void handle_shouldNotClearCacheAndResetDiscount_givenValidData() throws Exception {
    Customer customer = Customer.builder().nr(1100005).build();
    UserInfo user = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setCompanyName("company_A");
    CouponConditions conditions = new CouponConditions();
    conditions.setDiscountArticleId(1001388532);
    conditions.setCouponsCode(CPO1);
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);

    CouponValidationData data =
        CouponValidationData.builder().user(user).coupConditions(conditions).cart(cart).build();
    when(discountAmountValidator.isInValid(data)).thenReturn(false);
    doNothing().when(couponCacheService).clearCache(CPO1);
    CouponValidationData results = couponReValidateDiscountAmountStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
  }
}
