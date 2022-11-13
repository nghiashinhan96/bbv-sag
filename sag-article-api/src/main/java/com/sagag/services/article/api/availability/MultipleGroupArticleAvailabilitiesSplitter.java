package com.sagag.services.article.api.availability;

import com.sagag.services.domain.article.ArticleDocDto;

import java.util.List;

@FunctionalInterface
public interface MultipleGroupArticleAvailabilitiesSplitter {

  /**
   * Splits availabilities for multiple group articles.
   *
   * @param articles
   */
  void splitAvailabilities(List<ArticleDocDto> articles);
}
