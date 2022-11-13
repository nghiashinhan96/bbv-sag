package com.sagag.services.service.coupon.validation;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponArticleIdValidator;
import com.sagag.services.service.coupon.validator.CouponArticleIdValidatorTest;
import com.sagag.services.service.exception.coupon.InvalidAffiliateCouponException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

@EshopMockitoJUnitRunner
public class CouponValidateArticleIdStepTest {

  @InjectMocks
  private CouponValidateArticleIdStep couponValidateArticleIdStep;

  @Mock
  private CouponArticleIdValidator articleIdValidator;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = InvalidAffiliateCouponException.class)
  public void handle_shouldThrowsException_givenInvalidData() throws Exception {
    CouponValidationData data = CouponArticleIdValidatorTest.getInvalidData();
    when(articleIdValidator.isInValid(data)).thenReturn(true);
    couponValidateArticleIdStep.handle(data);
  }

  @Test
  public void handle_shouldReturnResultWithFilterdAppliedItem_givenValidData() throws Exception {
    ArticleDocDto firstArticleItem = new ArticleDocDto();
    firstArticleItem.setIdSagsys("sag_01");
    ArticleDocDto secondArticleItem = new ArticleDocDto();
    secondArticleItem.setIdSagsys("sag_02");
    ShoppingCartItem firstCartItem = new ShoppingCartItem();
    firstCartItem.setArticle(firstArticleItem);
    ShoppingCartItem secondeCartItem = new ShoppingCartItem();
    secondeCartItem.setArticle(secondArticleItem);
    CouponConditions conditions = new CouponConditions();
    conditions.setArticleId("sag_01");
    CouponValidationData data =
        CouponValidationData.builder().appliedItems(Arrays.asList(firstCartItem, secondeCartItem))
            .coupConditions(conditions).build();

    when(articleIdValidator.isInValid(data)).thenReturn(false);
    CouponValidationData stepResults = couponValidateArticleIdStep.handle(data);
    assertThat(stepResults.getAppliedItems().size(), Matchers.is(1));
    assertThat(stepResults.getAppliedItems().get(0).getArticle().getIdSagsys(),
        Matchers.is("sag_01"));
  }
}
