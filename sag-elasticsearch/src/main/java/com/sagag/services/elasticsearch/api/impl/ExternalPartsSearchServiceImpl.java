package com.sagag.services.elasticsearch.api.impl;

import com.google.common.collect.Lists;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.ExternalPartsSearchService;
import com.sagag.services.elasticsearch.criteria.ExternalPartsSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ExternalPartDoc;
import com.sagag.services.elasticsearch.dto.ExternalPartsResponse;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.extractor.ExternalPartResultsExtractors;
import com.sagag.services.elasticsearch.query.articles.article.ExternalPartsQueryBuilder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalPartsSearchServiceImpl extends AbstractElasticsearchService
    implements ExternalPartsSearchService {

  @Autowired
  private ExternalPartsQueryBuilder queryBuilder;

  @Override
  public String keyAlias() {
    return "ext_parts";
  }

  @Override
  public ExternalPartsResponse searchByCriteria(ExternalPartsSearchCriteria criteria) {
    final PageRequest pageable = PageUtils.MAX_PAGE;
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, pageable, index());
    final ResultsExtractor<ExternalPartsResponse> extractor =
        ExternalPartResultsExtractors.extract(pageable);
    return searchOrDefault(searchQuery, extractor, ExternalPartsResponse.empty());
  }

  @Override
  public List<ExternalPartDoc> searchByArtIds(List<String> artIds) {
    if (CollectionUtils.isEmpty(artIds)) {
      return Lists.newArrayList();
    }
    final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    boolQueryBuilder.must(QueryBuilders
        .termsQuery(Index.ExternalParts.ID_PIM.field(), artIds));

    log.debug("Query = {}", boolQueryBuilder);

    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withIndices(index())
        .withPageable(PageUtils.MAX_PAGE)
        .withQuery(boolQueryBuilder);
    return searchList(searchQueryBuilder.build(), ExternalPartDoc.class);
  }

}
