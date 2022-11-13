package com.sagag.services.article.api.executor;

/**
 * <p>
 * The interface executor to update ERP info to the list of target object.
 * </p>
 *
 * @param <S> the source generic object
 * @param <R> the result generic object
 *
 */
@FunctionalInterface
public interface IArticleSearchExecutor<S extends ArticleSearchCriteria, R> {

  /**
   * <p>
   * The service to execute implementation.
   * </p>
   *
   * @param source the object of {@link S}
   * @return the result object of {@link R}
   *
   */
  R execute(S source);

}
