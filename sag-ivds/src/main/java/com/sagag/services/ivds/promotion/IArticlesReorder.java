package com.sagag.services.ivds.promotion;

import com.sagag.services.domain.article.ArticleDocDto;

import java.util.List;

/**
 * Interface to provide some sorter for tyres articles.
 */
public interface IArticlesReorder {

  /**
   * Returns the list of sorted articles in first time.
   *
   * @param articles the list of articles.
   * @param brands the promotion brands
   * @return the sorted articles
   */
  List<ArticleDocDto> reorderFirstTime(List<ArticleDocDto> articles, List<String> brands);

  /**
   * Returns the list of sorted articles in batch.
   *
   * @param articles the batched of articles.
   * @param brands the promotion brands
   * @return the sorted articles
   */
  List<ArticleDocDto> reorderInBatch(List<ArticleDocDto> articles, List<String> brands);

}
