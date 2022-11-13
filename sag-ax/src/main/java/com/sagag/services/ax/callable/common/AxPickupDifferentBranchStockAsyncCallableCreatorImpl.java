package com.sagag.services.ax.callable.common;

import org.springframework.stereotype.Component;

import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.common.profiles.AxProfile;

@Component
@AxProfile
public class AxPickupDifferentBranchStockAsyncCallableCreatorImpl
  extends AxStockAsyncCallableCreatorImpl {

  @Override
  public AsyncUpdateMode asyncUpdateMode() {
    return AsyncUpdateMode.STOCK_PICKUP_DIFFERENT_BRANCH;
  }
}
