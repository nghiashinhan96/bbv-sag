package com.sagag.services.ax.price;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.ax.converter.price.AxPriceDiscountPromotionCalculatorImpl;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.number.impl.DefaultNumberFormatterImpl;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

/**
 * UT for AxPriceDiscountPromotionCalculatorImpl.
 */
@RunWith(SpringRunner.class)
public class AxPriceDiscountPromotionCalculatorTest {

  @Spy
  private LocaleContextHelper localeContextHelper;

  @Mock
  private DefaultNumberFormatterImpl numberFormatter;

  @InjectMocks
  private AxPriceDiscountPromotionCalculatorImpl priceDiscountPromotionCalculator;

  @Test
  public void testCalculatorPriceDiscountPromotionArticlePriceNullShouldDothing() {
    final PriceWithArticle articlePrice = null;
    priceDiscountPromotionCalculator.calculatorPriceDiscountPromotion(articlePrice, Optional.of(1));
    Assert.assertNull(articlePrice);
  }

  @Test
  public void testCalculatorPriceDiscountPromotionShouldCalculateAndRound() throws IOException {
    final PriceWithArticle articlePrice = initArticlePrice();
    PriceWithArticlePrice price = articlePrice.getPrice();
    final double discountInPercent = 56.5;
    final double promotionInPercent = 23.5;
    when(numberFormatter.getNumberFormatter("en_US"))
        .thenReturn(NumberFormat.getInstance(new Locale("en", "US")));
    priceDiscountPromotionCalculator.calculatorPriceDiscountPromotion(articlePrice, Optional.of(1));

    Assert.assertTrue(articlePrice.hasNet1Price());
    Assert.assertTrue(discountInPercent == price.getDiscountInPercent());
    Assert.assertTrue(promotionInPercent == price.getPromotionInPercent());
  }

  @Test
  public void testCalculatorPriceDiscountPromotionShouldCalculateAndRound2() throws IOException {
    final PriceWithArticle articlePrice = initArticlePrice();
    PriceWithArticlePrice price = articlePrice.getPrice();
    price.setNet1Price(101.5d);
    price.setNetPrice(90.30455d);
    final double promotionInPercent = 11.03;
    when(numberFormatter.getNumberFormatter("en_US"))
        .thenReturn(NumberFormat.getInstance(new Locale("en", "US")));
    priceDiscountPromotionCalculator.calculatorPriceDiscountPromotion(articlePrice, Optional.of(1));

    Assert.assertTrue(articlePrice.hasNet1Price());
    Assert.assertTrue(promotionInPercent == price.getPromotionInPercent());
  }

  @Test
  public void testCalculatorPriceDiscountPromotionShouldCalculatePromotionInPercentAndRound()
      throws IOException {
    final PriceWithArticle articlePrice = initArticlePrice();
    PriceWithArticlePrice price = articlePrice.getPrice();
    price.setStandardGrossPrice(6D);
    final double promotionInPercent = 23.5;
    when(numberFormatter.getNumberFormatter("en_US"))
        .thenReturn(NumberFormat.getInstance(new Locale("en", "US")));
    priceDiscountPromotionCalculator.calculatorPriceDiscountPromotion(articlePrice, Optional.of(1));

    Assert.assertTrue(articlePrice.hasNet1Price());
    Assert.assertTrue(29 == price.getDiscountInPercent());
    Assert.assertTrue(promotionInPercent == price.getPromotionInPercent());

  }

  private PriceWithArticle initArticlePrice() throws IOException {
    final File file =
        new File(FilenameUtils.separatorsToSystem("src/test/resources/price/price.json"));
    return SagJSONUtil
        .convertJsonToObject(FileUtils.readFileToString(file, "UTF-8"), PriceWithArticle.class);
  }

}
