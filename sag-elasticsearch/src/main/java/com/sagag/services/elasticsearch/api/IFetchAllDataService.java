package com.sagag.services.elasticsearch.api;

import com.sagag.services.common.utils.PageUtils;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;

public interface IFetchAllDataService<T> {

  /**
   * Returns the all data of repository.
   */
  List<T> getAll();

  default NativeSearchQueryBuilder allSearchQueryBuilder(String... indicies) {
   return new NativeSearchQueryBuilder()
       .withIndices(indicies)
       .withQuery(QueryBuilders.matchAllQuery())
       .withPageable(PageUtils.MAX_PAGE);
  }

  default Function<String[], SearchQuery> matchAllSearchQueryByIndicies() {
    return indicies -> {
      Assert.notEmpty(indicies, "The given indicies must not be empty.");
      return allSearchQueryBuilder(indicies).build();
    };
  }

  default Function<String, SearchQuery> matchAllSearchQueryByIndex() {
    return index -> {
      Assert.hasText(index, "The given index must not be null.");
      return matchAllSearchQueryByIndicies().apply(new String[] { index });
    };
  }

}
