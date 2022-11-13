package com.sagag.services.ax.converter.price;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.ax.domain.AxPriceWithArticle;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

@Component
public class FinalCustomerPriceConverterImpl extends AbstractPriceConverter {

  @Override
  public PriceWithArticle convert(AxPriceWithArticle articlePrice, PriceRequest request,
      PriceDisplayTypeEnum priceEnum) {
    if (!articlePrice.isPriceFound()) {
      return PriceWithArticle.builder().articleId(articlePrice.getArticleId()).build();
    }
    final Optional<Integer> quantity =
        request.findQuantityByArticleId(articlePrice.getArticleId());
    final PriceWithArticle price = updatePriceByCommonRule(articlePrice, quantity, priceEnum);
    setNoPriceIfGrossEqualsNetPrice(price);
    setPriceAndTotalGross(price, price.getGrossPrice(), quantity);
    return price;
  }

  @Override
  public boolean isValidConverter(boolean isFinalCustomerUser) {
    return isFinalCustomerUser;
  }

}
