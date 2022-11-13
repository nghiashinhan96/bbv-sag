package com.sagag.services.ivds.executor;

import com.sagag.services.domain.article.ArticleDocDto;

import java.util.List;

public interface IvdsAdditionalArticleTaskExecutor {

  /**
   * Updates additional information for final page of articles.
   *
   * @param articles articles
   * @param isFilterArticleBefore
   * @param isCallPriceRequest
   * @return the result articles
   */
  void executeTask(List<ArticleDocDto> articles, boolean isFilterArticleBefore,
      boolean isCallPriceRequest);

}
