package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.IMap;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.PageUtils;

import org.apache.commons.collections4.ListUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CacheDataProcessor {

  private static final int MAX_SIZE = SagConstants.MAX_PAGE_SIZE;

  protected <V> List<V> getAllCacheData(Function<Pageable, Page<V>> dataProvider) {
    return getAllCacheData(dataProvider, MAX_SIZE);
  }

  protected <V> List<V> getAllCacheData(Function<Pageable, Page<V>> dataProvider, int size) {
    Pageable pageable = PageUtils.defaultPageable(0, size);
    Page<V> firstPage = dataProvider.apply(pageable);

    final List<V> cacheData = new ArrayList<>();
    cacheData.addAll(firstPage.getContent());
    while (firstPage.hasNext()) {
      pageable = pageable.next();
      firstPage = dataProvider.apply(pageable);
      cacheData.addAll(firstPage.getContent());
    }

    return ListUtils.emptyIfNull(cacheData);
  }

  /**
   * Refreshes all data of index on the cache.
   *
   * @return the {@link IMap}
   */
  public abstract void refreshCacheAll();

}
