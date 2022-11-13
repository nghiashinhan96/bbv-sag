package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleNumberDeepLinkQueryBuilder
  extends AbstractArticleQueryBuilder<KeywordArticleSearchCriteria> {

  private static final int ARTICLE_ID_LENGTH = 10;
  private static final String[] ART_ID = { ArticleField.ID_SAGSYS.value() };

  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final String articleNr = criteria.getText();
    final String[] affNameLocks = criteria.getAffNameLocks();
    final boolean isSaleOnBehalf = criteria.isSaleOnBehalf();
    final BoolQueryBuilder queryBuilder = isArticleIdFormat(articleNr)
        ? applyCommonQueryBuilder(affNameLocks, isSaleOnBehalf)
            .apply(QueryBuilders.boolQuery().must(articleIdAndArtNumQueryBuilder(articleNr)))
        : applyCommonQueryBuilder(affNameLocks, isSaleOnBehalf).apply(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery(ArticleField.ART_NUMBER.value(), articleNr)));
    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(queryBuilder).build();
  }

  private boolean isArticleIdFormat(String text) {
    return StringUtils.isNumeric(text) && text.length() == ARTICLE_ID_LENGTH;
  }

  private BoolQueryBuilder articleIdAndArtNumQueryBuilder(String searchText) {
    List<QueryBuilder> queries = new ArrayList<>();
    queries.add(createFilterQueryForArticleNumber(searchText));
    queries.add(createFilterQueryForArticleId(searchText, ART_ID));

    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    queries.stream().forEach(boolQuery::should);
    return QueryBuilders.boolQuery().must(boolQuery);
  }
}
