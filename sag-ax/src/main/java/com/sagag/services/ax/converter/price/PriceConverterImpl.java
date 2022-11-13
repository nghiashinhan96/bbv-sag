package com.sagag.services.ax.converter.price;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.price.PriceDiscountPromotionCalculator;
import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.ax.domain.AxPriceWithArticle;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

@Component
public class PriceConverterImpl extends AbstractPriceConverter {

  @Autowired
  private PriceDiscountPromotionCalculator priceDiscountPromotionCalculator;

  @Override
  public PriceWithArticle convert(AxPriceWithArticle articlePrice, PriceRequest request,
      PriceDisplayTypeEnum priceEnum) {
    if (!articlePrice.isPriceFound()) {
      // Init price object for calculating the vat-rate later
      PriceWithArticlePrice initPriceWithArticlePrice = PriceWithArticlePrice.builder().build();
      return PriceWithArticle.builder().articleId(articlePrice.getArticleId())
          .price(initPriceWithArticlePrice).build();
    }
    final Optional<Integer> quantity = request.findQuantityByArticleId(articlePrice.getArticleId());
    PriceWithArticle updatePriceByCommonRule =
        updatePriceByCommonRule(articlePrice, quantity, priceEnum);
    priceDiscountPromotionCalculator.calculatorPriceDiscountPromotion(updatePriceByCommonRule,
        quantity);
    return updatePriceByCommonRule;
  }

  @Override
  public boolean isValidConverter(boolean isFinalCustomerUser) {
    return !isFinalCustomerUser;
  }

}
