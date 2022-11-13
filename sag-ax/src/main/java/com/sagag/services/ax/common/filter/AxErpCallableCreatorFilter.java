package com.sagag.services.ax.common.filter;

import java.util.function.BiPredicate;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;

public interface AxErpCallableCreatorFilter
  extends BiPredicate<ErpCallableCreator, ArticleSearchCriteria> {

}
