package com.sagag.services.article.api.executor;

import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequest;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceResponseItem;

import java.util.List;

@FunctionalInterface
public interface DisplayedPriceSearchExecutor {

  /**
   * Executes display prices for articles.
   *
   * @param request
   * @return display prices
   */
  List<DisplayedPriceResponseItem> execute(DisplayedPriceRequest request);

}
