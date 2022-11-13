package com.sagag.services.elasticsearch.multisearch;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.profiles.AnArtExternalArticleServiceMode;

import org.apache.commons.collections4.ListUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AnArtExternalArticleServiceMode
public class AutonetArticleMultiSearchExecutor extends AbstractElasticsearchService {

  @LogExecutionTime
  public SearchResponse[] execute(SearchQuery[] queries, Pageable pageable) {
    MultiSearchRequest request = new MultiSearchRequest();
    Client client = getEsClient();
    for (SearchQuery query : queries) {
      SearchRequestBuilder searchRequest = client.prepareSearch(query.getIndices()
        .toArray(new String[0]))
        .setSearchType(query.getSearchType()).setTypes(query.getTypes().toArray(new String[0]))
        .setQuery(query.getQuery())
        .setFrom((int) pageable.getOffset())
        .setSize(pageable.getPageSize());
      ListUtils.emptyIfNull(query.getAggregations()).forEach(searchRequest::addAggregation);
      request.add(searchRequest.request());
    }

    final MultiSearchResponse response = client.multiSearch(request).actionGet();
    return Arrays.asList(response.getResponses()).stream()
        .map(MultiSearchResponse.Item::getResponse)
        .toArray(SearchResponse[]::new);
  }

  @Override
  public String keyAlias() {
    throw new UnsupportedOperationException("No support get alias by key");
  }
}
