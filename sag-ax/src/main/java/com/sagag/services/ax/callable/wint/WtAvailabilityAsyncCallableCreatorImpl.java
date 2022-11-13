package com.sagag.services.ax.callable.wint;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.ax.callable.AvailabilityAsyncCallableCreator;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.article.ArticleDocDto;

@Component
@SbProfile
public class WtAvailabilityAsyncCallableCreatorImpl implements AvailabilityAsyncCallableCreator {

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria input, Object... objects) {
    CallableCreator.setupThreadContext(objects);
    return () -> updateStockAndAvailability();
  }

  private List<ArticleDocDto> updateStockAndAvailability() {
    return Collections.emptyList();
  }

  @Override
  public AsyncUpdateMode asyncUpdateMode() {
    return AsyncUpdateMode.STOCK;
  }
}
