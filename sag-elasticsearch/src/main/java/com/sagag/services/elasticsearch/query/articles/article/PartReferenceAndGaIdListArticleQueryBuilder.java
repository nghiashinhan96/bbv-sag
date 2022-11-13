package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.PartReferenceAndGaIdListSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PartReferenceAndGaIdListArticleQueryBuilder
  extends AbstractArticleQueryBuilder<PartReferenceAndGaIdListSearchCriteria> {

  @Override
  public SearchQuery buildQuery(PartReferenceAndGaIdListSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final List<String> prnrs = criteria.getPrnrs();
    final List<String> gaIds = criteria.getGaIds();
    final String[] affNameLocks = criteria.getAffNameLocks();
    final boolean isSaleOnBehalf = criteria.isSaleOnBehalf();
    final List<String> lowercasePrnrs =
      prnrs.stream().map(StringUtils::lowerCase).collect(Collectors.toList());

    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
      .must(QueryBuilders.nestedQuery(ArticleField.PARTS_NUMBER.code(),
        QueryBuilders.boolQuery()
          .must(QueryBuilders.termsQuery(ArticleField.PARTS_NUMBER.value(), lowercasePrnrs)),
        ScoreMode.None));
    queryBuilder.must(QueryBuilders.termsQuery(ArticleField.GA_ID.value(), gaIds));

    final BoolQueryBuilder query = applyCommonQueryBuilder(affNameLocks, isSaleOnBehalf).apply(queryBuilder);

    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(query).build();
  }
}
