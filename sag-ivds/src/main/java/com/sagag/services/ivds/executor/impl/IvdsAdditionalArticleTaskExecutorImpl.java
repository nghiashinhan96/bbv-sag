package com.sagag.services.ivds.executor.impl;

import com.sagag.services.article.api.article.ArticleLockFilterPredicate;
import com.sagag.services.article.api.article.StockAndPriceArticleConsumer;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.executor.IvdsAdditionalArticleTaskExecutor;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IvdsAdditionalArticleTaskExecutorImpl implements IvdsAdditionalArticleTaskExecutor {

  @Autowired(required = false)
  private StockAndPriceArticleConsumer stockAndPriceArticleConsumer;

  @Autowired(required = false)
  private ArticleLockFilterPredicate articleLockFilterPredicate;

  @Override
  public void executeTask(List<ArticleDocDto> articles,
      boolean isFilterArticleBefore, boolean isCallPriceRequest) {
    if (CollectionUtils.isEmpty(articles)) {
      return;
    }

    // Update stock information for articles that has no price
    if (isCallPriceRequest && stockAndPriceArticleConsumer != null) {
      articles.forEach(stockAndPriceArticleConsumer);
    }
    if (isFilterArticleBefore && articleLockFilterPredicate != null) {
      // Ignore filter article lock if user not turn on filter article before flag.
      articles.removeIf(articleLockFilterPredicate);
    }
  }

}
