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
import com.sagag.services.service.coupon.validator.CouponBrandValidator;
import com.sagag.services.service.coupon.validator.CouponBrandValidatorTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponReValidateBrandStepTest {

  private static final String HONDA = "HONDA";

  private static final String AUDI = "AUDI";

  @InjectMocks
  private CouponReValidateBrandStep couponReValidateBrandStep;

  @Mock
  private CouponBrandValidator brandValidator;

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
    CouponValidationData data = CouponBrandValidatorTest.getInvalidData();
    data.setCart(cart);
    when(brandValidator.isInValid(data)).thenReturn(true);
    doNothing().when(couponCacheService).clearCache(data.getCouponCode());
    CouponValidationData results = couponReValidateBrandStep.handle(data);

    verify(couponCacheService, times(1)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(0.0));
  }

  @Test
  public void handle_shouldNotClearCacheAndResetDiscount_givenValidData() throws Exception {
    ArticleDocDto audiArticleItem = new ArticleDocDto();
    audiArticleItem.setProductBrand(AUDI);
    ArticleDocDto hondaArticleItem = new ArticleDocDto();
    hondaArticleItem.setProductBrand(HONDA);
    ShoppingCartItem audiCartItem = new ShoppingCartItem();
    audiCartItem.setArticle(audiArticleItem);
    ShoppingCartItem hondaCartItem = new ShoppingCartItem();
    hondaCartItem.setArticle(hondaArticleItem);
    CouponConditions conditions = new CouponConditions();
    conditions.setBrands(AUDI);
    CouponValidationData data =
        CouponValidationData.builder().appliedItems(Arrays.asList(audiCartItem, hondaCartItem))
            .coupConditions(conditions).build();

    ShoppingCart cart = new ShoppingCart();
    cart.setDiscount(-100.0);
    data.setCart(cart);

    when(brandValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponReValidateBrandStep.handle(data);

    verify(couponCacheService, times(0)).clearCache(data.getCouponCode());
    assertNotNull(results);
    assertThat(results.getCart().getDiscount(), Matchers.is(-100.0));
    assertThat(results.getAppliedItems().size(), Matchers.is(1));
    assertThat(results.getAppliedItems().get(0).getArticle().getProductBrand(), Matchers.is(AUDI));
  }
}
