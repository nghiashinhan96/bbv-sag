package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.LicensePlateField;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LicensePlateQueryBuilder
    extends AbstractArticleQueryBuilder<KeywordArticleSearchCriteria> {

  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
      String... indices) {

    final MatchQueryBuilder searchQuery =
        QueryBuilders.matchQuery(LicensePlateField.LP.value(), criteria.getText());

    log.debug("Used index of License Plate = {} \nQuery = {}", indices, searchQuery);

    return new NativeSearchQueryBuilder().withPageable(defaultPageable(pageable))
        .withIndices(indices).withQuery(searchQuery).build();
  }

}
