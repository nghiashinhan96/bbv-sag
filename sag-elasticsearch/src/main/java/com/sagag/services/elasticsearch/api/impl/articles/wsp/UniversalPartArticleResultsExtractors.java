package com.sagag.services.elasticsearch.api.impl.articles.wsp;

import com.sagag.services.common.contants.UniversalPartConstants;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.UniversalPartArticleResponse;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.extractor.ResultsExtractorUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public final class UniversalPartArticleResultsExtractors {

  /**
   * Extracts ES result in universal part.
   *
   * @param pageable the pagination
   * @return the result extractor of {@link UniversalPartArticleResponse}
   */
  public static ResultsExtractor<UniversalPartArticleResponse> extractUniversalPart(
      final Pageable pageable) {
    return response -> {

      final Page<ArticleDoc> articles =
          ResultsExtractorUtils.extractPageResult(response, ArticleDoc.class, pageable);

      // Get custom aggregation list from search result
      final Map<String, List<Object>> curAggs =
          ResultsExtractorUtils.extractAggregations(response.getAggregations());
      if (MapUtils.isEmpty(curAggs)) {
        return UniversalPartArticleResponse.builder().articles(articles).build();
      }

      // Build the map of aggregations
      final Map<String, List<Object>> aggregations = new HashMap<>();

      final List<Object> genArtIds = curAggs.get(ArticleField.GA_ID.name()).stream()
          .map(Object::toString).collect(Collectors.toList());

      aggregations.put(UniversalPartConstants.SUPPLIER_MAP_NAME, getSuppliers(curAggs));
      aggregations.put(UniversalPartConstants.GENART_MAP_NAME, genArtIds);

      return UniversalPartArticleResponse.builder().articles(articles).aggregations(aggregations)
          .build();
    };
  }

  private static List<Object> getSuppliers(Map<String, List<Object>> curAggs) {
    return curAggs.get(ArticleField.ID_PRODUCT_BRAND.name()).stream().sorted()
        .collect(Collectors.toList());
  }

}
