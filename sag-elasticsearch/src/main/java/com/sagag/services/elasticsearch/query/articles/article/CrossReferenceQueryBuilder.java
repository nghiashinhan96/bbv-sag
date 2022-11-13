package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.CrossReferenceArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.enums.Index.Article;
import com.sagag.services.elasticsearch.enums.IndexFieldType;
import com.sagag.services.elasticsearch.parser.FreetextStringParser;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryBuilders;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CrossReferenceQueryBuilder extends AbstractArticleQueryBuilder<CrossReferenceArticleSearchCriteria>
    implements IAggregationBuilder {

  @Autowired
  private FreetextStringParser freetextStringParser;

  private static final String CROSS_REFERENCE_PART_TYPE = "IAM OR PCC OR iam OR pcc";

  @Override
  public SearchQuery buildQuery(CrossReferenceArticleSearchCriteria criteria, Pageable pageable, String... indices) {
    final BoolQueryBuilder searchQuery = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
        .apply(crossReferenceBoolQueryBuilder(criteria));

    aggFilterBuilders.forEach(builder -> builder.addFilter(searchQuery, criteria));
    log.debug("Query = {}", searchQuery);

    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(searchQuery).build();
  }

  private BoolQueryBuilder crossReferenceBoolQueryBuilder(final CrossReferenceArticleSearchCriteria criteria) {
    log.debug("Searching article cross-reference: ArtNr {}, brandId {}", criteria.getArtNr(), criteria.getBrandId());

    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    boolQuery.must(createFilterQueryForPartType());
    boolQuery.must(createFilterQueryForPartPrNr(criteria.getArtNr()));

    return QueryBuilders.boolQuery().must(boolQuery);
  }

  private QueryBuilder createFilterQueryForPartType() {
    final Map<String, Float> attributeBoost = new HashMap<>();
    attributeBoost.put(Index.Article.PARTS_TYPE.fullQField(), 5.0f);
    return buildFilteredQuery(CROSS_REFERENCE_PART_TYPE, new String[] { Article.PARTS_TYPE.fullQField() },
        attributeBoost);
  }

  private QueryBuilder createFilterQueryForPartPrNr(String artNr) {
    final String normalizedText = freetextStringParser.apply(artNr, new Object[0]);
    final Map<String, Float> attributeBoost = new HashMap<>();
    attributeBoost.put(Index.Article.PNRN.fullQField(), 4.0f);
    return buildFilteredQuery(normalizedText, new String[] { Article.PNRN.fullQField()}, attributeBoost);
  }

  private static QueryBuilder buildFilteredQuery(final String freetext, final String[] fields,
      final Map<String, Float> attrBoost) {
    return ArticleQueryBuilders.filterQuery(attrBoost, IndexFieldType.NESTED, ArticleField.PARTS, true)
        .apply(freetext, fields);
  }
}
