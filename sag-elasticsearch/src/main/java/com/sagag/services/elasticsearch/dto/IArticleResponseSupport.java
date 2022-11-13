package com.sagag.services.elasticsearch.dto;

import com.sagag.services.elasticsearch.domain.article.ArticleDoc;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Interface to provide some support functional.
 */
public interface IArticleResponseSupport {

  /**
   * Returns the result to check has articles.
   *
   */
  boolean hasArticles();

  /**
   * Returns the total articles in response.
   *
   */
  long totalArticles();

  /**
   * Returns the result to allow view articles info.
   *
   */
  boolean allowViewArticles();

  /**
   * Returns the result to check has aggregations.
   *
   */
  boolean hasAggregations();

  /**
   * Returns the list of aggregations.
   *
   * @return the map of objects
   */
  Map<String, List<Object>> aggregations();

  default boolean hasArticles(Page<ArticleDoc> articles) {
    return Objects.nonNull(articles) && articles.hasContent();
  }
}