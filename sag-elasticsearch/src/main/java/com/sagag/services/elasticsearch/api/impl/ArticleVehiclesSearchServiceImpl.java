package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.ArticleVehiclesSearchService;
import com.sagag.services.elasticsearch.domain.article.ArticleVehicles;
import com.sagag.services.elasticsearch.enums.Index;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ArticleVehiclesSearchServiceImpl extends AbstractElasticsearchService
  implements ArticleVehiclesSearchService {

  @Override
  public String keyAlias() {
    return "article_vehicle";
  }

  @Override
  public List<ArticleVehicles> searchArticleVehiclesByArtId(String articleId) {
    log.debug("searching vehicle usages by artid = {}", articleId);
    if (StringUtils.isBlank(articleId)) {
      return Collections.emptyList();
    }

    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        .must(QueryBuilders.nestedQuery(Index.ArticleVehicles.ARTICLE_ARTID.path(),
            QueryBuilders.termQuery(Index.ArticleVehicles.ARTICLE_ARTID.field(), articleId),
            ScoreMode.None));

    final FieldSortBuilder vehSort = SortBuilders
        .fieldSort(Index.ArticleVehicles.VEHICLES_SORT.field())
        .order(SortOrder.ASC);

    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withIndices(index())
        .withPageable(PageUtils.MAX_PAGE)
        .withQuery(queryBuilder)
        .withSort(vehSort);
    return searchList(searchQueryBuilder.build(), ArticleVehicles.class);
  }
}
