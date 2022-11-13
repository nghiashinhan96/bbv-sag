package com.sagag.services.elasticsearch.query.articles.article.parts;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.ArticlePartType;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class PccArticleQueryBuilder extends
    AbstractArticleQueryBuilder<KeywordArticleSearchCriteria> implements IAggregationBuilder {

  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {
    final String pnrn = criteria.getText();
    Assert.hasText(pnrn, "The PCC code should not be null or blank");

    final BoolQueryBuilder queryBuilder =
        applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf()).apply(PartArticleSearchQueryBuilder
            .partQueryBuilder(pnrn, ArticlePartType.PCC, criteria.isUsePartsExt()));

    aggFilterBuilders.forEach(builder -> builder.addFilter(queryBuilder, criteria));
    log.debug("Query = {}", queryBuilder);

    final NativeSearchQueryBuilder searchQueryBuilder =
        ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(queryBuilder);

    aggregated(searchQueryBuilder, ArticleField.SUPPLIER_RAW);

    return searchQueryBuilder.build();
  }

}
