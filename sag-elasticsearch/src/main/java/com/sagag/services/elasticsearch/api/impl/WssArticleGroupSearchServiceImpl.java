package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.WssArticleGroupSearchService;
import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchByArticleGroupIdCriteria;
import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchByLeafIdCriteria;
import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchCriteria;
import com.sagag.services.elasticsearch.domain.wss.WssArticleGroupDoc;
import com.sagag.services.elasticsearch.dto.WssArticleGroupSearchResponse;
import com.sagag.services.elasticsearch.query.wss.WssArticleGroupResultExtractors;
import com.sagag.services.elasticsearch.query.wss.WssArticleGroupSearchByArticleGroupIdQueryBuilder;
import com.sagag.services.elasticsearch.query.wss.WssArticleGroupSearchByLeafIdQueryBuilder;
import com.sagag.services.elasticsearch.query.wss.WssArticleGroupSearchQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * Elasticsearch service implementation class for WSS Article Group document.
 */
@Service
@Slf4j
public class WssArticleGroupSearchServiceImpl extends AbstractElasticsearchService
    implements WssArticleGroupSearchService {

  @Autowired
  private WssArticleGroupSearchQueryBuilder wssArticleGroupSearchQueryBuilder;

  @Autowired
  private WssArticleGroupSearchByLeafIdQueryBuilder wssArticleGroupSearchByLeafIdQueryBuilder;

  @Autowired
  private WssArticleGroupSearchByArticleGroupIdQueryBuilder wssArticleGroupSearchByArticleGroupIdQueryBuilder;

  @Override
  public String keyAlias() {
    return "wss_artgrp";
  }

  @Override
  public List<WssArticleGroupDoc> getTop10ArticleGroup() {
    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
        .withIndices(index())
        .withPageable(PageUtils.DEF_PAGE)
        .withQuery(QueryBuilders.matchAllQuery());
    return searchList(queryBuilder.build(), WssArticleGroupDoc.class);
  }

  @Override
  public WssArticleGroupSearchResponse searchWssArticleGroupByCriteria(
      WssArticleGroupSearchCriteria criteria, Pageable pageable) {
    log.debug("Searching WSS article group in ES by criteria = {}", criteria);
    Assert.notNull(criteria, "The given search criteria must not be null");
    final SearchQuery query =
        wssArticleGroupSearchQueryBuilder.buildQuery(criteria, pageable, index());

    return search(query, WssArticleGroupResultExtractors.extractByFiltering(pageable));
  }

  @Override
  public Optional<WssArticleGroupDoc> findByLeafId(String leafId) {
    log.debug("Searching WSS article group description in ES by leaf id = {}", leafId);
    WssArticleGroupSearchByLeafIdCriteria criteria =
        WssArticleGroupSearchByLeafIdCriteria.builder().leafId(leafId).build();
    final SearchQuery query =
        wssArticleGroupSearchByLeafIdQueryBuilder.buildQuery(criteria, Pageable.unpaged(), index());
    WssArticleGroupSearchResponse searchResult =
        search(query, WssArticleGroupResultExtractors.extractByFiltering(Pageable.unpaged()));
    if (!searchResult.getArticleGroups().hasContent()) {
      return Optional.empty();
    }

    return Optional.of(searchResult.getArticleGroups().getContent().get(0));
  }

  @Override
  public Optional<WssArticleGroupDoc> findByArticleGroupId(String articleGroupId) {
    log.debug("Searching WSS article group description in ES by article group id = {}",
        articleGroupId);
    WssArticleGroupSearchByArticleGroupIdCriteria criteria =
        WssArticleGroupSearchByArticleGroupIdCriteria.builder().articleGroupId(articleGroupId)
            .build();
    final SearchQuery query = wssArticleGroupSearchByArticleGroupIdQueryBuilder.buildQuery(criteria,
        Pageable.unpaged(), index());
    WssArticleGroupSearchResponse searchResult =
        search(query, WssArticleGroupResultExtractors.extractByFiltering(Pageable.unpaged()));
    if (!searchResult.getArticleGroups().hasContent()) {
      return Optional.empty();
    }

    return Optional.of(searchResult.getArticleGroups().getContent().get(0));
  }

  @Override
  public List<WssArticleGroupDoc> getAll() {
    return searchStream(allSearchQueryBuilder(index()).build(), WssArticleGroupDoc.class);
  }
}
