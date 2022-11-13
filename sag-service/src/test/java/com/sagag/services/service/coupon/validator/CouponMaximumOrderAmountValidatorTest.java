package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ArticlePriceItem;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponMaximumOrderAmountValidatorTest {

  @InjectMocks
  private CouponMaximumOrderAmountValidator validator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shouldBeValid_givenValidData() throws Exception {
    CouponValidationData data = getValidData();
    assertFalse(validator.isInValid(data));
  }

  @Test
  public void isInValid_shouldBeInvalid_givenInvalidData() throws Exception {
    CouponValidationData data = getInvalidData();
    assertTrue(validator.isInValid(data));
  }

  public static CouponValidationData getValidData() {
    ArticlePriceItem price = new ArticlePriceItem(null, null, null, null, null, null, null, 300.00,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    ShoppingCartItem cartItem = new ShoppingCartItem();
    ArticleDocDto articleItem = new ArticleDocDto();
    cartItem.setArticle(articleItem);
    cartItem.setPriceItem(price);
    ShoppingCart cart = new ShoppingCart();
    cart.setItems(Arrays.asList(cartItem));
    CouponConditions conditions = new CouponConditions();
    conditions.setMaximumDiscount(500.00);
    return CouponValidationData.builder().cart(cart).coupConditions(conditions).build();
  }


  public static CouponValidationData getInvalidData() {
    ArticlePriceItem price = new ArticlePriceItem(null, null, null, null, null, null, null, 300.00,
        null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    ShoppingCartItem cartItem = new ShoppingCartItem();
    ArticleDocDto articleItem = new ArticleDocDto();
    cartItem.setArticle(articleItem);
    cartItem.setPriceItem(price);
    ShoppingCart cart = new ShoppingCart();
    cart.setItems(Arrays.asList(cartItem));
    CouponConditions conditions = new CouponConditions();
    conditions.setMaximumDiscount(200.00);
    return CouponValidationData.builder().cart(cart).coupConditions(conditions).build();
  }
}
