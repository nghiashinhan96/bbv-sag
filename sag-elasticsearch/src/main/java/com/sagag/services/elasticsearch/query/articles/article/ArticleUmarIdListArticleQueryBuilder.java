package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.ArticleIdListSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleUmarIdListArticleQueryBuilder
  extends AbstractArticleQueryBuilder<ArticleIdListSearchCriteria> {

  @Override
  public SearchQuery buildQuery(ArticleIdListSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final List<String> umarIds = criteria.getArticleIdList();
    final String[] affNameLocks = criteria.getAffNameLocks();
    final boolean isSaleOnBehalf = criteria.isSaleOnBehalf();
    BoolQueryBuilder queryBuilder = applyCommonQueryBuilder(affNameLocks, isSaleOnBehalf)
      .apply(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery(
        ArticleField.ID_UMSART.value(), umarIds)));
    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(queryBuilder).build();
  }
}
