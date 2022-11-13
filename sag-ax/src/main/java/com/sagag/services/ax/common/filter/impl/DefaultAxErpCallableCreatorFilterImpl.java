package com.sagag.services.ax.common.filter.impl;

import org.springframework.stereotype.Component;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.ax.common.filter.AxErpCallableCreatorFilter;
import com.sagag.services.common.profiles.AxProfile;

@Component
@AxProfile
public class DefaultAxErpCallableCreatorFilterImpl implements AxErpCallableCreatorFilter {

  @Override
  public boolean test(ErpCallableCreator callableCreator, ArticleSearchCriteria criteria) {
    return callableCreator != null && callableCreator.asyncUpdateMode() != null
        && callableCreator.asyncUpdateMode().isValid(criteria);
  }

}
