package com.sagag.services.article.api.price.finalcustomer;

import static org.mockito.Mockito.when;

import com.sagag.services.article.api.price.finalcustomer.impl.FinalCustomerPriceCalculatorImpl;
import com.sagag.services.article.api.price.finalcustomer.impl.WssMarginValueByArticleGroupFinderImpl;
import com.sagag.services.article.api.price.finalcustomer.impl.WssMarginValueByBrandFinderImpl;
import com.sagag.services.article.api.utils.ArticleInfoTestUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FinalCustomerPriceCalculatorTest {

  private static final double GROSS_PRICE = 3d;

  private static final double MARGIN_BY_ARTICLE_GROUP_VALUE = 0.15d;

  private static final double MARGIN_BY_BRAND_VALUE = 0.2d;

  private static final int MARGIN_GROUP_2 = 2;

  public static final int CUSTOMER_5132364_ORG_ID = 137;

  @InjectMocks
  private FinalCustomerPriceCalculatorImpl finalCustomerPriceCalculator;

  @Mock
  private WssMarginValueByArticleGroupFinderImpl wssMarginArticleGroupFinder;

  @Mock
  private WssMarginValueByBrandFinderImpl wssMarginBrandFinder;

  @Test
  public void testHandleFinalCustomerNetPrice_nullArticleShouldReturnEmpty() {
    final Optional<Double> finalCustomerNetPriceOpt =
        finalCustomerPriceCalculator.handleFinalCustomerNetPrice(null, null, false, false, null);
    Assert.assertFalse(finalCustomerNetPriceOpt.isPresent());
  }

  @Test
  public void testHandleFinalCustomerNetPrice_wholesalerNotOfferNetPriceShouldReturnEmpty()
      throws IOException {
    final ArticleDocDto articleDocDto = ArticleInfoTestUtils.initArticleDocDto();
    final Optional<Double> finalCustomerNetPriceOpt = finalCustomerPriceCalculator
        .handleFinalCustomerNetPrice(articleDocDto, null, false, true, null);
    Assert.assertFalse(finalCustomerNetPriceOpt.isPresent());
  }

  @Test
  public void testHandleFinalCustomerNetPrice_finalCustomerHasNoNetPriceShouldReturnEmpty()
      throws IOException {
    final ArticleDocDto articleDocDto = ArticleInfoTestUtils.initArticleDocDto();
    final Optional<Double> finalCustomerNetPriceOpt = finalCustomerPriceCalculator
        .handleFinalCustomerNetPrice(articleDocDto, null, false, true, null);
    Assert.assertFalse(finalCustomerNetPriceOpt.isPresent());
  }

  @Test
  public void testHandleFinalCustomerNetPrice_shouldReturnFinalCustomerNetPrice()
      throws IOException {
    final ArticleDocDto articleDocDto = ArticleInfoTestUtils.initArticleDocDto();
    final List<String> articleGroups = articleDocDto.getArticleGroups();
    final Integer brandId = Integer.valueOf(articleDocDto.getIdDlnr());
    when(wssMarginArticleGroupFinder.findMarginValue(CUSTOMER_5132364_ORG_ID, MARGIN_GROUP_2, null,
        articleGroups)).thenReturn(MARGIN_BY_ARTICLE_GROUP_VALUE);
    when(wssMarginBrandFinder.findMarginValue(CUSTOMER_5132364_ORG_ID, MARGIN_GROUP_2, brandId,
        Collections.emptyList())).thenReturn(MARGIN_BY_BRAND_VALUE);
    final Optional<Double> finalCustomerNetPriceOpt =
        finalCustomerPriceCalculator.handleFinalCustomerNetPrice(articleDocDto,
            CUSTOMER_5132364_ORG_ID, true, true, MARGIN_GROUP_2);
    Assert.assertTrue(finalCustomerNetPriceOpt.isPresent());
    final Double finalCustomerNetPrice = finalCustomerNetPriceOpt.get();
    final PriceWithArticlePrice articlePrice = articleDocDto.getPrice().getPrice();
    final Double netPrice = articlePrice.getNetPrice();
    Assert.assertTrue(finalCustomerNetPrice > netPrice);
  }

  @Test
  public void testHandleFinalCustomerNetPrice_shouldReturnFinalCustomerGrossPrice()
      throws IOException {
    final ArticleDocDto articleDocDto = ArticleInfoTestUtils.initArticleDocDto();
    final PriceWithArticlePrice articlePrice = articleDocDto.getPrice().getPrice();
    articlePrice.setGrossPrice(GROSS_PRICE);
    final List<String> articleGroups = articleDocDto.getArticleGroups();
    final Integer brandId = Integer.valueOf(articleDocDto.getIdDlnr());
    when(wssMarginArticleGroupFinder.findMarginValue(CUSTOMER_5132364_ORG_ID, MARGIN_GROUP_2, null,
        articleGroups)).thenReturn(MARGIN_BY_ARTICLE_GROUP_VALUE);
    when(wssMarginBrandFinder.findMarginValue(CUSTOMER_5132364_ORG_ID, MARGIN_GROUP_2, brandId,
        Collections.emptyList())).thenReturn(MARGIN_BY_BRAND_VALUE);
    final Optional<Double> finalCustomerNetPriceOpt =
        finalCustomerPriceCalculator.handleFinalCustomerNetPrice(articleDocDto,
            CUSTOMER_5132364_ORG_ID, true, true, MARGIN_GROUP_2);
    Assert.assertTrue(finalCustomerNetPriceOpt.isPresent());
    final Double finalCustomerNetPrice = finalCustomerNetPriceOpt.get();
    Assert.assertTrue(Double.compare(GROSS_PRICE, finalCustomerNetPrice) == 0);
  }

}
