package com.sagag.services.ax.converter.price;

import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.ax.domain.AxPriceWithArticle;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

@FunctionalInterface
public interface PriceConverter {

  PriceWithArticle convert(AxPriceWithArticle articlePrice, PriceRequest request, 
      PriceDisplayTypeEnum priceEnum);
}
