package com.sagag.services.ivds.request.filter;

@FunctionalInterface
public interface ArticleSearchCriteriaConverter<T> {

  T toCriteria();

}
