package com.sagag.services.ax.executor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;

/**
 * Class to implement to execute depot or recycle article info update from AX service.
 *
 */
@Component
public class DefaultAttachedArticleSearchExecutor extends AbstractAttachedArticleSearchExecutor<Map<String, ArticleDocDto>> {

  @Autowired
  private FinalCustomerPriceCalculator finalCustomerPriceCalculator;

  @Override
  @LogExecutionTime
  public Map<String, ArticleDocDto> execute(AttachedArticleSearchCriteria criteria) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notNull(criteria.getAffiliate(), "The given affiliate must not be null");
    Assert.notNull(criteria.getCustNr(), "The given customer number must not be null");
    Assert.notEmpty(criteria.getAttachedArticleRequestList(),
        "The given attached article list must not be empty");

    final List<ArticleDocDto> filteredArticles = updateAttachedArticles(criteria);

    updatePrice(filteredArticles, criteria);

   return bindingArticlesByKey(filteredArticles, criteria.getAttachedArticleRequestList(), false);
  }

  @Override
  public FinalCustomerPriceCalculator finalCustomerPriceCalculator() {
    return finalCustomerPriceCalculator;
  }

  @Override
  public boolean isCallShoppingCart() {
    return false;
  }

}
