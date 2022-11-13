package com.sagag.services.elasticsearch.api.impl;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.UnitreeSearchService;
import com.sagag.services.elasticsearch.criteria.unitree.KeywordUnitreeSearchCriteria;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.unitrees.unitree.freetext.FreetextUnitreeQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitreeSearchServiceImpl extends AbstractElasticsearchService
    implements UnitreeSearchService {

  @Autowired
  private FreetextUnitreeQueryBuilder freetextUnitreeQueryBuilder;

  @Override
  public String keyAlias() {
    return "unitree";
  }

  @Override
  public List<UnitreeDoc> getAllUnitreeCompact() {
    final SearchQuery searchQueryBuilder = new NativeSearchQueryBuilder()
        .withSourceFilter(
            new FetchSourceFilterBuilder().withIncludes(Index.UnitreeDoc.TREE_ID.path(),
                Index.UnitreeDoc.TREE_NAME.path(), Index.UnitreeDoc.TREE_IMAGE.path(),
                Index.UnitreeDoc.TREE_SORT.path(), Index.UnitreeDoc.TREE_EXTERNAL_SERVICE.path(),
                Index.UnitreeDoc.EXTERNAL_SERVICE_ATTRIBUTE.path()).build())
        .withQuery(matchAllQuery()).withPageable(PageUtils.MAX_PAGE).withIndices(index()).build();

    return searchList(searchQueryBuilder, UnitreeDoc.class);
  }

  @Override
  public Optional<UnitreeDoc> getUnitreeByUnitreeId(String unitreeId) {
    if (StringUtils.isBlank(unitreeId)) {
      return Optional.empty();
    }

    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        .must(QueryBuilders.matchQuery(Index.UnitreeDoc.TREE_ID.path(), unitreeId));

    final SearchQuery searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
        .withQuery(queryBuilder).withPageable(PageUtils.MAX_PAGE).withIndices(index()).build();

    return searchList(searchQueryBuilder, UnitreeDoc.class).stream().findFirst();
  }

  @Override
  public Optional<UnitreeDoc> getUnitreeByLeafId(final String leafId) {
    if (StringUtils.isBlank(leafId)) {
      return Optional.empty();
    }
    final BoolQueryBuilder queryBuilder =
        QueryBuilders.boolQuery().must(QueryBuilders.nestedQuery(Index.Unitree.LEAF_ID.path(),
            QueryBuilders.termQuery(Index.Unitree.LEAF_ID.field(), leafId), ScoreMode.None));

    final SearchQuery searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(matchAllQuery())
        .withQuery(queryBuilder).withPageable(PageUtils.MAX_PAGE).withIndices(index()).build();
    return searchList(searchQueryBuilder, UnitreeDoc.class).stream().findFirst();
  }

  @Override
  public Page<UnitreeDoc> search(KeywordUnitreeSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return Page.empty();
    }

    final SearchQuery searchQuerie =
        freetextUnitreeQueryBuilder.buildQuery(criteria, pageable, index());
    return searchPage(searchQuerie, UnitreeDoc.class);

  }
}
