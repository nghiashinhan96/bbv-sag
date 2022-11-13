package com.sagag.services.article.api.attachedarticle;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;

import java.util.List;

public interface AttachedArticleHandler {

  public void buildMemoList(List<ArticleDocDto> filteredArticles, ArticleSearchCriteria criteria);

  public void doFilterAvailabilityFgasCases(List<ArticleDocDto> finalArticles,
      ArticleSearchCriteria criteria);

}
