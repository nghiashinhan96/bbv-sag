package com.sagag.services.ax.article;

import java.util.List;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;

public class DefaultAttachedArticleHandlerImpl extends AbstractAttachedArticleHandler {

  @Override
  public void buildMemoList(List<ArticleDocDto> filteredArticles, ArticleSearchCriteria criteria) {
    // Do nothing by default
  }

  @Override
  public void doFilterAvailabilityFgasCases(List<ArticleDocDto> finalArticles,
      ArticleSearchCriteria criteria) {
    // Do nothing by default
  }

}
