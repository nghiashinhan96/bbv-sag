package com.sagag.services.service.coupon.revalidation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponGenartIdValidator;
import com.sagag.services.service.coupon.validator.CouponGenartIdValidatorTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponReValidateGenartIdStepTest {

  private static final String COUPON_CODE = "CP01";
  private static final String GA_01 = "ga_01";

  @InjectMocks
  private CouponReValidateGenartIdStep couponReValidateGenartIdStep;

  @Mock
  private CouponGenartIdValidator genartIdValidator;

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
    CouponValidationData data = CouponGenartIdValidatorTest.getInvalidData();
    data.setCart(cart);
    data.setCoupConditions(conditions);
    when(genartIdValidator.isInValid(data)).thenReturn(true);
    doNothing().when(couponCacheService).clearCache(COUPON_CODE);
    CouponValidationData results = couponReValidateGenartIdStep.handle(data);

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
    conditions.setArticleCategories(GA_01);
    ShoppingCartItem cartItem1 = new ShoppingCartItem();
    ArticleDocDto articleItem1 = new ArticleDocDto();
    articleItem1.setGaId(GA_01);
    cartItem1.setArticle(articleItem1);
    ShoppingCartItem cartItem2 = new ShoppingCartItem();
    ArticleDocDto articleItem2 = new ArticleDocDto();
    articleItem2.setGaId("ga_02");
    cartItem2.setArticle(articleItem2);
    CouponValidationData data =
        CouponValidationData.builder().appliedItems(Arrays.asList(cartItem1, cartItem2))
            .coupConditions(conditions).cart(cart).build();

    when(genartIdValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponReValidateGenartIdStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
    assertThat(results.getAppliedItems().size(), Matchers.is(1));
    assertThat(results.getAppliedItems().get(0).getArticle().getGaId(), Matchers.is(GA_01));
  }
}
