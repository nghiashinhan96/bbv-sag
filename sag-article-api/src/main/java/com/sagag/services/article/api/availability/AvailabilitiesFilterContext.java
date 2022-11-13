package com.sagag.services.article.api.availability;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;
import java.util.List;

@FunctionalInterface
public interface AvailabilitiesFilterContext {

  /**
   * Filters the availabilities.
   *
   * @param originalArticles
   * @param criteria
   * @return the updated list of articles.
   */
  List<ArticleDocDto> doFilterAvailabilities(List<ArticleDocDto> originalArticles,
    ArticleSearchCriteria criteria);
}
