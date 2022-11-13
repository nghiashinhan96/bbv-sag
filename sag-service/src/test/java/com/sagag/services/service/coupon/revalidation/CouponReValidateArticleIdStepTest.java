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
import com.sagag.services.service.coupon.validator.CouponAffiliateValidatorTest;
import com.sagag.services.service.coupon.validator.CouponArticleIdValidator;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponReValidateArticleIdStepTest {

  private static final String SAG_02 = "sag_02";

  private static final String SAG_01 = "sag_01";

  @InjectMocks
  private CouponReValidateArticleIdStep couponReValidateArticleIdStep;

  @Mock
  private CouponArticleIdValidator articleIdValidator;

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
    CouponValidationData data = CouponAffiliateValidatorTest.getInvalidData();
    data.setCart(cart);
    when(articleIdValidator.isInValid(data)).thenReturn(true);
    doNothing().when(couponCacheService).clearCache(data.getCouponCode());
    CouponValidationData results = couponReValidateArticleIdStep.handle(data);

    verify(couponCacheService, times(1)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(0.0));
  }

  @Test
  public void handle_shouldNotClearCacheAndResetDiscount_givenValidData() throws Exception {
    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);
    ArticleDocDto firstArticleItem = new ArticleDocDto();
    firstArticleItem.setIdSagsys(SAG_01);
    ArticleDocDto secondArticleItem = new ArticleDocDto();
    secondArticleItem.setIdSagsys(SAG_02);
    ShoppingCartItem firstCartItem = new ShoppingCartItem();
    firstCartItem.setArticle(firstArticleItem);
    ShoppingCartItem secondeCartItem = new ShoppingCartItem();
    secondeCartItem.setArticle(secondArticleItem);
    CouponConditions conditions = new CouponConditions();
    conditions.setArticleId(SAG_01);
    CouponValidationData data = CouponValidationData.builder().cart(cart)
        .appliedItems(Arrays.asList(firstCartItem, secondeCartItem)).coupConditions(conditions)
        .build();

    when(articleIdValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponReValidateArticleIdStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
    assertThat(results.getAppliedItems().size(), Matchers.is(1));
    assertThat(results.getAppliedItems().get(0).getArticle().getIdSagsys(), Matchers.is(SAG_01));
  }
}
