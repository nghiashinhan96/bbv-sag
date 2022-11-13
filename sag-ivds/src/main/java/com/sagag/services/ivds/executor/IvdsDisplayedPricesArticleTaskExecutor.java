package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequest;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceResponseItem;
import java.util.List;

public interface IvdsDisplayedPricesArticleTaskExecutor {

  /**
   * Returns display prices for given articles
   *
   * @param user
   * @param request
   * @return
   */
  List<DisplayedPriceResponseItem> executeTaskWithArticleDisplayPrices(UserInfo user,
      DisplayedPriceRequest request);
}
