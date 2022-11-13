package com.sagag.services.article.api.executor.callable;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.domain.article.ArticleDocDto;

import java.util.List;

@FunctionalInterface
public interface ErpCallableCreator
    extends CallableCreator<ArticleSearchCriteria, List<ArticleDocDto>> {

  /**
   * Returns the mode will be executed as async mode.
   *
   * @return the enum of <code>AsyncUpdateMode</code>
   */
  default AsyncUpdateMode asyncUpdateMode() {
    return null;
  }

}
