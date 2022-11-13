package com.sagag.services.ivds.filter.aggregation;

import com.sagag.services.domain.article.ArticleFilterItem;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
public class SubAggregationResultUtils {

  public static final int MAX_INDEX_ADD_SHOW_MORE = 3;

  public List<ArticleFilterItem> addShowMoreItem(final List<ArticleFilterItem> subFilterItems) {
    if (CollectionUtils.isEmpty(subFilterItems)) {
      return Collections.emptyList();
    }
    final List<ArticleFilterItem> updatedSubFilterItems = new ArrayList<>();
    final int size = subFilterItems.size();
    IntStream.range(0, size).boxed()
    .forEach(index -> {
      ArticleFilterItem subArticleFilterItem = subFilterItems.get(index);
      subArticleFilterItem.setShown(index < MAX_INDEX_ADD_SHOW_MORE);
      updatedSubFilterItems.add(subArticleFilterItem);
    });
    if (size > MAX_INDEX_ADD_SHOW_MORE) {
      updatedSubFilterItems.add(ArticleFilterItem.showMoreFilterItem());
    }
    return updatedSubFilterItems;
  }
}
