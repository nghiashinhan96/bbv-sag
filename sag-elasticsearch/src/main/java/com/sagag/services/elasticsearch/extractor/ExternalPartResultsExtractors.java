package com.sagag.services.elasticsearch.extractor;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.elasticsearch.converter.AggregationBucketListConverters;
import com.sagag.services.elasticsearch.domain.article.ExternalPartDoc;
import com.sagag.services.elasticsearch.dto.ExternalPartsResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

@UtilityClass
public class ExternalPartResultsExtractors {

  @LogExecutionTime
  public static ResultsExtractor<ExternalPartsResponse> extract(final Pageable pageable) {
    return response -> {
      final Page<ExternalPartDoc> externalParts =
          ResultsExtractorUtils.extractPageResult(response, ExternalPartDoc.class, pageable);
      if (!externalParts.hasContent()) {
        return ExternalPartsResponse.empty();
      }
      final Map<String, Aggregation> curAggs = Optional.ofNullable(response.getAggregations())
        .filter(Objects::nonNull).map(Aggregations::asMap).orElseGet(Collections::emptyMap);
      if (MapUtils.isEmpty(curAggs)) {
        return ExternalPartsResponse.of(externalParts);
      }

      final Map<String, List<SagBucket>> aggregations = new HashMap<>();
      curAggs.forEach((name, aggregation) -> aggregations.putAll(
          AggregationBucketListConverters.aggregationConverter().apply(name, aggregation)));

      return ExternalPartsResponse.builder().externalParts(externalParts).aggregations(aggregations).build();
    };
  }
}