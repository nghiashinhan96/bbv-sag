package com.sagag.services.elasticsearch.query.wss;

import com.sagag.services.elasticsearch.domain.wss.WssArticleGroupDoc;
import com.sagag.services.elasticsearch.dto.WssArticleGroupSearchResponse;
import com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils;

import lombok.experimental.UtilityClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@UtilityClass
public class WssArticleGroupResultExtractors {
  public ResultsExtractor<WssArticleGroupSearchResponse> extractByFiltering(Pageable pageable) {
    return response -> {
      final Page<WssArticleGroupDoc> wssArticleGroupDocs =
          ResultsExtractorUtils.extractPageResult(response, WssArticleGroupDoc.class, pageable);
      if (!wssArticleGroupDocs.hasContent()) {
        return WssArticleGroupSearchResponse.builder().articleGroups(Page.empty())
            .aggregations(Collections.emptyMap()).build();
      }
      final WssArticleGroupSearchResponse result = new WssArticleGroupSearchResponse();
      result.setArticleGroups(
          new PageImpl<>(wssArticleGroupDocs.getContent(), pageable, wssArticleGroupDocs.getTotalElements()));
      final Map<String, List<Object>> extractedAggregations =
          ResultsExtractorUtils.extractAggregations(response.getAggregations());

      // Lowercase key map
      result.setAggregations(extractedAggregations.entrySet().stream()
          .collect(Collectors.toMap(agg -> agg.getKey().toLowerCase(), Entry::getValue)));
      return result;
    };
  }
}
