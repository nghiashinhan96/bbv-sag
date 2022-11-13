package com.sagag.services.ax.converter.price;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.price.PriceDiscountPromotionCalculator;
import com.sagag.services.common.number.NumberFormatter;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

@Service
@EnablePriceDiscountPromotion(true)
public class AxPriceDiscountPromotionCalculatorImpl implements PriceDiscountPromotionCalculator {

  private static final String LOCALE_PERCENTAGE_FORMAT = "en_US";

  private static final DecimalFormat ONE_FRACTION_DIGIT_DECIMAL_FORMAT = new DecimalFormat("0.#");

  private static final DecimalFormat TWO_FRACTION_DIGIT_DECIMAL_FORMAT = new DecimalFormat("0.##");

  @Autowired
  private NumberFormatter numberFormatter;

  @Override
  public void calculatorPriceDiscountPromotion(PriceWithArticle priceWithArticle,
      Optional<Integer> quantity) {
    if (Objects.isNull(priceWithArticle)) {
      return;
    }
    PriceWithArticlePrice price = priceWithArticle.getPrice();
    price.setDiscountPrice(NumberUtils.DOUBLE_ZERO);
    price.setDiscountInPercent(NumberUtils.DOUBLE_ZERO);
    price.setTotalDiscountPrice(NumberUtils.DOUBLE_ZERO);

    if (!priceWithArticle.hasNetPrice()) {
      return;
    }

    final double netPrice = price.getNetPrice().doubleValue();
    double discountInPercent = 0;
    if (price.getStandardGrossPrice() == null
        || price.getStandardGrossPrice().equals(NumberUtils.DOUBLE_ZERO)) {
      return;
    }

    final double grossPrice = price.getStandardGrossPrice().doubleValue();
    if (!priceWithArticle.hasNet1Price() || (netPrice > price.getNet1Price().doubleValue())) {
      discountInPercent = 100 - (100 / grossPrice * netPrice);
      price.setDiscountInPercent(roundPercentage(discountInPercent));
      return;
    }

    final double net1Price = price.getNet1Price().doubleValue();
    final double promotionInPercent = 100 - (100 / net1Price * netPrice);
    discountInPercent = 100 - (100 / grossPrice * net1Price);
    price.setPromotionInPercent(roundPercentage(promotionInPercent));
    price.setDiscountInPercent(roundPercentage(discountInPercent));
  }

  private double roundPercentage(double number) {
    if (Objects.isNull(number)) {
      return NumberUtils.DOUBLE_MINUS_ONE;
    }

    NumberFormat formatter = numberFormatter.getNumberFormatter(LOCALE_PERCENTAGE_FORMAT);
    Double rounded = Double.valueOf(formatter.format(number));

    return trimTrailZero(rounded);
  }

  private double trimTrailZero(double number) {
    if (Objects.isNull(number)) {
      return NumberUtils.DOUBLE_MINUS_ONE;
    }
    return Math.max(Double.parseDouble(TWO_FRACTION_DIGIT_DECIMAL_FORMAT.format(number)),
        Double.parseDouble(ONE_FRACTION_DIGIT_DECIMAL_FORMAT.format(number)));
  }

}
