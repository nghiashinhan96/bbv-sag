package com.sagag.services.service.coupon.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponValidationData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponBrandValidatorTest {

  @InjectMocks
  private CouponBrandValidator validator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void isInValid_shouldBeValid_givenValidData() throws Exception {
    ShoppingCartItem cartItem = new ShoppingCartItem();
    ArticleDocDto articleItem = new ArticleDocDto();
    articleItem.setProductBrand("AUDI");
    cartItem.setArticle(articleItem);
    CouponConditions conditions = new CouponConditions();
    conditions.setBrands("AUDI,TOYOTA");
    CouponValidationData data = CouponValidationData.builder().appliedItems(Arrays.asList(cartItem))
        .coupConditions(conditions).build();
    assertFalse(validator.isInValid(data));
  }

  @Test
  public void isInValid_shouldBeInValid_givenInValidData() throws Exception {
    CouponValidationData data = getInvalidData();
    assertTrue(validator.isInValid(data));
  }

  public static CouponValidationData getInvalidData() {
    ShoppingCartItem cartItem = new ShoppingCartItem();
    ArticleDocDto articleItem = new ArticleDocDto();
    articleItem.setProductBrand("AUDI");
    cartItem.setArticle(articleItem);
    CouponConditions conditions = new CouponConditions();
    conditions.setBrands("TOYOTA");
    CouponValidationData data = CouponValidationData.builder().appliedItems(Arrays.asList(cartItem))
        .coupConditions(conditions).build();
    return data;
  }
}
