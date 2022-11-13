package com.sagag.services.article.api.availability;

@FunctionalInterface
public interface IArticleAvailabilityState {

  /**
   * Returns the article availability result object.
   *
   * @return the object of <code>ArticleAvailabilityResult</code>
   */
  ArticleAvailabilityResult toResult();

}
