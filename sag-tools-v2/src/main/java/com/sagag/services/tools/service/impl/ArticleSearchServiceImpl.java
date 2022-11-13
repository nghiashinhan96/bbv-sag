package com.sagag.services.tools.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.elasticsearch.ArticleDoc;
import com.sagag.services.tools.service.ArticleIndex;
import com.sagag.services.tools.service.ArticleSearchService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@OracleProfile
public class ArticleSearchServiceImpl implements ArticleSearchService {

  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;

  @Override
  public Optional<ArticleDoc> searchArticleByIdUmsart(final String idUmsart) {
    log.debug("Search article by id umsart = {}", idUmsart);
    if (StringUtils.isBlank(idUmsart)) {
      return Optional.empty();
    }
    final NativeSearchQuery searchQuery = buildArticleSearchQueryByIdUmsart(idUmsart);
    final List<ArticleDoc> vehicles =
      elasticsearchTemplate.queryForList(searchQuery, ArticleDoc.class);
    return vehicles.stream().findFirst();
  }

  private static NativeSearchQuery buildArticleSearchQueryByIdUmsart(final String idUmsart) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.must(QueryBuilders.matchQuery(ArticleField.ID_UMSART.getField(), idUmsart));
    return new NativeSearchQueryBuilder().withQuery(queryBuilder)
      .withIndices(ArticleIndex.ARTICLES_DE.getIndexName()).build();
  }
}
