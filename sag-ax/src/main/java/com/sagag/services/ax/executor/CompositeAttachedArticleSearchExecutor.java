package com.sagag.services.ax.executor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchExternalExecutor;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.domain.article.ArticleDocDto;

/**
 * Class to implement to execute depot or recycle article info update from AX service.
 *
 */
@Component
@Primary
public class CompositeAttachedArticleSearchExecutor extends AbstractAttachedArticleSearchExecutor<Map<String, ArticleDocDto>> {

  @Autowired(required = false)
  private List<AttachedArticleSearchExternalExecutor<Map<String, ArticleDocDto>>> articleSearchExternalExecutors;

  @Override
  public FinalCustomerPriceCalculator finalCustomerPriceCalculator() {
    throw new UnsupportedOperationException(
        "Unsupported calculate price final customer for empty article search executor");
  }

  @Override
  public Map<String, ArticleDocDto> execute(AttachedArticleSearchCriteria source) {
    if (articleSearchExternalExecutors == null) {
      return Collections.emptyMap();
    }
    return articleSearchExternalExecutors.stream()
        .filter(execute -> execute.isCallShoppingCart() == source.isCallShoppingCart()).findFirst()
        .map(execute -> execute.execute(source)).orElse(Collections.emptyMap());
  }

  @Override
  public boolean isCallShoppingCart() {
    throw new UnsupportedOperationException(
        "Unsupported is call shopping cart");
  }


}
