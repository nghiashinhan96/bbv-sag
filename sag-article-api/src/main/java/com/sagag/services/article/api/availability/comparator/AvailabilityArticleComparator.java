package com.sagag.services.article.api.availability.comparator;

import com.google.common.collect.Lists;
import com.sagag.services.domain.article.ArticleDocDto;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Comparator;
import java.util.List;

public interface AvailabilityArticleComparator extends Comparator<ArticleDocDto> {

  /**
   * Returns the perfect match articles.
   *
   * @param unsortedArticles
   *
   * @return sorted articles list
   */
  default List<ArticleDocDto> sortPerfectMatchArticles(List<ArticleDocDto> unsortedArticles) {
    if (CollectionUtils.isEmpty(unsortedArticles)) {
      return Lists.newArrayList();
    }
    unsortedArticles.sort(this);
    return Lists.newArrayList(unsortedArticles);
  }
}
