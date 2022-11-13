package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.parser.OnlyArticleNrStringParser;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArtNrAndSupplierIdQueryBuilder extends
    AbstractArticleQueryBuilder<KeywordArticleSearchCriteria> implements IAggregationBuilder {

  @Autowired
  private OnlyArticleNrStringParser strParser;

  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {
    final BoolQueryBuilder searchQuery =
        applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
            .apply(onlyArtNumAndSupplierIdQueryBuilder(criteria.getText(), criteria.getIdDlnr()));

    log.debug("Query = {}", searchQuery);

    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices)
      .withQuery(searchQuery).build();
  }

  private BoolQueryBuilder onlyArtNumAndSupplierIdQueryBuilder(String searchText, String idDlnr) {
    String modifiedSearchText = strParser.apply(searchText, null);
    List<QueryBuilder> queries = new ArrayList<>();
    queries.add(createFilterQueryForArticleNumber(modifiedSearchText));

    if (!StringUtils.isBlank(idDlnr)) {
      queries.add(createMatchQueryForSupplierId(idDlnr));
    }

    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    queries.stream().forEach(boolQuery::must);
    return QueryBuilders.boolQuery().must(boolQuery);
  }

  private static QueryBuilder createMatchQueryForSupplierId(final String idDlnr) {
    MatchQueryBuilder matchQueryBuilder =
      QueryBuilders.matchQuery(ArticleField.ID_DLNR.value(), idDlnr);
    if (IndexFieldType.NONE_NESTED != IndexFieldType.NESTED) {
      return matchQueryBuilder;
    }
    return QueryBuilders.nestedQuery(ArticleField.REFS.value(), matchQueryBuilder, ScoreMode.None);
  }

}
