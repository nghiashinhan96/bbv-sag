package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponBrandValidator;
import com.sagag.services.service.coupon.validator.CouponBrandValidatorTest;
import com.sagag.services.service.exception.coupon.InvalidAffiliateCouponException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponValidateBrandStepTest {

  private static final String HONDA = "HONDA";

  private static final String AUDI = "AUDI";

  @InjectMocks
  private CouponValidateBrandStep couponValidateBrandStep;

  @Mock
  private CouponBrandValidator brandValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = InvalidAffiliateCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponBrandValidatorTest.getInvalidData();
    when(brandValidator.isInValid(data)).thenReturn(true);
    couponValidateBrandStep.handle(data);
  }

  @Test
  public void handle_shouldReturnResultWithFilterdAppliedItem_givenValidData() throws Exception {
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
    when(brandValidator.isInValid(data)).thenReturn(false);
    CouponValidationData results = couponValidateBrandStep.handle(data);
    assertNotNull(results);
    assertThat(results.getAppliedItems().size(), Matchers.is(1));
    assertThat(results.getAppliedItems().get(0).getArticle().getProductBrand(), Matchers.is(AUDI));
  }
}
