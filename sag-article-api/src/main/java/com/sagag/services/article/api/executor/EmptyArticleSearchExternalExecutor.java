package com.sagag.services.article.api.executor;

import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerDeliveryTimeCalculator;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.common.profiles.NoneErpProfile;
import com.sagag.services.domain.article.ArticleDocDto;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoneErpProfile
public class EmptyArticleSearchExternalExecutor implements ArticleSearchExternalExecutor {

  @Override
  public List<ArticleDocDto> execute(ArticleSearchCriteria source) {
    return ListUtils.emptyIfNull(source.getArticles());
  }

  @Override
  public FinalCustomerPriceCalculator finalCustomerPriceCalculator() {
    throw new UnsupportedOperationException(
        "Unsupported calculate price final customer for empty article search executor");
  }

  @Override
  public FinalCustomerDeliveryTimeCalculator finalCustomerDeliveryCalculator() {
    throw new UnsupportedOperationException(
        "Unsupported calculate availabilities final customer for empty article search executor");
  }

}
