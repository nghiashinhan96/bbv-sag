package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.CategoryService;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.enums.Index;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Elasticsearch service implementation for Category document.
 */
@Service
public class CategoryServiceImpl extends AbstractElasticsearchService implements CategoryService {

  @Override
  public String keyAlias() {
    return "categories";
  }

  @Override
  public Page<CategoryDoc> getAllCategories(Pageable pageable) {
    final NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withIndices(index())
      .withPageable(pageable)
      .withQuery(QueryBuilders.matchAllQuery());
    return searchPage(queryBuilder.build(), CategoryDoc.class);
  }

  @Override
  public List<CategoryDoc> getCategoriesByLeaftxt(final String leaftxt) {
    if (StringUtils.isBlank(leaftxt)) {
      return Collections.emptyList();
    }
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        .should(QueryBuilders.matchQuery(Index.Category.LEAFTXT.field(), leaftxt)
            .fuzziness(Fuzziness.AUTO).operator(Operator.AND))
        .should(QueryBuilders.matchPhrasePrefixQuery(Index.Category.LEAFTXT.field(), leaftxt))
        .should(
            QueryBuilders.matchPhrasePrefixQuery(Index.Category.NODE_KEYWORDS.field(), leaftxt));

    final NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
      .withIndices(index())
      .withPageable(PageUtils.MAX_PAGE)
      .withQuery(queryBuilder);
    return searchList(searchQuery.build(), CategoryDoc.class);
  }

  @Override
  public List<CategoryDoc> getAll() {
    return searchStream(matchAllSearchQueryByIndex().apply(index()), CategoryDoc.class);
  }

}
