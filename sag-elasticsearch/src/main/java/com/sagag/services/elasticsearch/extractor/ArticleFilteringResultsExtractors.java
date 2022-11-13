package com.sagag.services.elasticsearch.extractor;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.elasticsearch.converter.AggregationBucketListConverters;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class ArticleFilteringResultsExtractors {

  @LogExecutionTime
  public static ResultsExtractor<ArticleFilteringResponse> extract(final Pageable pageable) {
    return response -> {
      final Page<ArticleDoc> articles =
          ResultsExtractorUtils.extractPageResult(response, ArticleDoc.class, pageable);
      if (!articles.hasContent()) {
        return ArticleFilteringResponse.empty();
      }
      final Map<String, Aggregation> curAggs = Optional.ofNullable(response.getAggregations())
        .filter(Objects::nonNull).map(Aggregations::asMap).orElseGet(Collections::emptyMap);
      if (MapUtils.isEmpty(curAggs)) {
        return ArticleFilteringResponse.of(articles);
      }

      final Map<String, List<SagBucket>> aggregations = new HashMap<>();
      curAggs.forEach((name, aggregation) -> aggregations.putAll(
          AggregationBucketListConverters.aggregationConverter().apply(name, aggregation)));

      return ArticleFilteringResponse.builder().articles(articles)
        .aggregations(aggregations).build();
    };
  }

  protected Map<String, List<Object>> extractAggregations(final Aggregations aggregations) {
    if (Objects.isNull(aggregations)) {
      return Collections.emptyMap();
    }
    return ResultsExtractorUtils.toAggregationBucketsMap(aggregations.asMap());
  }

}