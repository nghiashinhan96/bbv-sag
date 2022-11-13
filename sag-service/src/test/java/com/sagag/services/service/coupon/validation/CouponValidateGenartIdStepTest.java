package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponGenartIdValidator;
import com.sagag.services.service.coupon.validator.CouponGenartIdValidatorTest;
import com.sagag.services.service.exception.coupon.InvalidAffiliateCouponException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponValidateGenartIdStepTest {

  private static final String GA_01 = "ga_01";

  @InjectMocks
  private CouponValidateGenartIdStep couponValidateGenartIdStep;

  @Mock
  private CouponGenartIdValidator genartIdValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = InvalidAffiliateCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponGenartIdValidatorTest.getInvalidData();
    when(genartIdValidator.isInValid(data)).thenReturn(true);
    couponValidateGenartIdStep.handle(data);
  }

  @Test
  public void handle_shouldReturnResultWithFilterdAppliedItem_givenValidData() throws Exception {
    ShoppingCartItem cartItem1 = new ShoppingCartItem();
    ArticleDocDto articleItem1 = new ArticleDocDto();
    articleItem1.setGaId(GA_01);
    cartItem1.setArticle(articleItem1);

    ShoppingCartItem cartItem2 = new ShoppingCartItem();
    ArticleDocDto articleItem2 = new ArticleDocDto();
    articleItem2.setGaId("ga_02");
    cartItem2.setArticle(articleItem2);

    CouponConditions conditions = new CouponConditions();
    conditions.setArticleCategories(GA_01);
    CouponValidationData data = CouponValidationData.builder()
        .appliedItems(Arrays.asList(cartItem1, cartItem2)).coupConditions(conditions).build();
    CouponValidationData results = couponValidateGenartIdStep.handle(data);
    assertNotNull(results);
    assertThat(results.getAppliedItems().size(), Matchers.is(1));
    assertThat(results.getAppliedItems().get(0).getArticle().getGaId(), Matchers.is(GA_01));
  }
}
