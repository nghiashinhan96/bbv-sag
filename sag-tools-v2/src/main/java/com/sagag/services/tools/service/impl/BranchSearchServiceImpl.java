package com.sagag.services.tools.service.impl;

import java.util.Objects;
import java.util.Optional;

import com.sagag.services.tools.domain.elasticsearch.BranchDoc;
import com.sagag.services.tools.service.BranchSearchService;
import com.sagag.services.tools.support.Index;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * Elasticsearch service implementation for branch document.
 */
@Service
@Slf4j
public class BranchSearchServiceImpl extends AbstractElasticsearchService
    implements BranchSearchService {

  @Override
  public Optional<BranchDoc> searchByBranchNr(final Integer branchNr, final String indexBranchES) {
    log.debug("searching branch by branch number = {}", branchNr);
    if (Objects.isNull(branchNr)) {
      throw new IllegalArgumentException("The branch number must not be empty");
    }
    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery(Index.Branch.BRANCH_NR.getValue(), branchNr));

    final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(indexBranchES).withQuery(boolQuery).build();

    return getEsTemplate().queryForList(searchQuery, BranchDoc.class).stream().findFirst();
  }
}
