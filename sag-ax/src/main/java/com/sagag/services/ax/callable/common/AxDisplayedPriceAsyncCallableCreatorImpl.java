package com.sagag.services.ax.callable.common;

import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.price.DisplayedPriceRequestCriteria;
import com.sagag.services.ax.domain.AxDisplayedPriceRequestBody;
import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

@Component
@AxProfile
public class AxDisplayedPriceAsyncCallableCreatorImpl
    implements CallableCreator<DisplayedPriceRequestCriteria, Void> {

  @Autowired
  private ArticleExternalService articleExtService;

  @Override
  public Callable<Void> create(DisplayedPriceRequestCriteria requestBody, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updatePrice((AxDisplayedPriceRequestBody) requestBody);
    };
  }

  private Void updatePrice(AxDisplayedPriceRequestBody requestBody) {

    final String articleId = requestBody.getArticleId();
    final Map<String, PriceWithArticle> priceRes =
        articleExtService.searchPrices(requestBody.getCompanyName(),
            requestBody.getAxPriceRequest(), requestBody.isFinalCustomerUser());

    final DisplayedPriceDto oepPriceDto = AxPriceUtils.calculateOepDisplayedPrice(
        priceRes.get(articleId), requestBody.getVatRate(), requestBody.getBrandId(),
        requestBody.getBrand(), requestBody.getAmountNumber());

    requestBody.setPrice(oepPriceDto);
    return null;
  }

}
