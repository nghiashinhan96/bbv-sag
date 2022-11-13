package com.sagag.services.article.api.executor.callable;

import com.sagag.services.domain.sag.erp.BulkArticleResult;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public interface ChunkedRequestProcessor<T> {

  /**
   * Returns max request size by chunked.
   *
   */
  int maxRequestSize();

  /**
   * Split partitions by size and proceed the processor.
   *
   * @param originalList
   * @param processor
   * @return the result list.
   */
  default List<T> processPartitionsByList(List<T> originalList, UnaryOperator<List<T>> processor) {
    List<List<T>> partitions = partition(originalList);
    return partitions.stream().map(ArrayList::new)
        .map(processor).flatMap(List::stream).collect(Collectors.toList());
  }

  /**
   * Split partitions by size and proceed the processor.
   *
   * @param originalList
   * @param processor
   * @return the result list.
   */
  default void processPartitionsByTargetMap(List<String> articleIds,
      Function<List<String>, Map<String, BulkArticleResult>> processor,
      Map<String, BulkArticleResult> targetMap) {
    if (CollectionUtils.isEmpty(articleIds)) {
      return;
    }
    if (targetMap == null) {
      targetMap = new HashMap<>();
    }
    List<List<String>> partitions = partition(articleIds);
    partitions.stream().map(processor).forEach(targetMap::putAll);
  }

  default <E> List<List<E>> partition(List<E> list) {
    if (CollectionUtils.isEmpty(list)) {
      return new ArrayList<>();
    }
    return ListUtils.partition(list, maxRequestSize());
  }

}
