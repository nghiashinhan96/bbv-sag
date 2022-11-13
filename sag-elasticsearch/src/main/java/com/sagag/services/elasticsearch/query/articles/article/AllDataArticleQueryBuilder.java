package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.AllDataArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
public class AllDataArticleQueryBuilder
  extends AbstractArticleQueryBuilder<KeywordArticleSearchCriteria>
  implements IAggregationBuilder {

  private static final String[] MULTI_MATCH_FIELDS = {
    AllDataArticleField.ARTICLE_NUMBER.field(),
    AllDataArticleField.USAGE_NUMBER.field(),
    AllDataArticleField.REFERENCE_NUMBER.field(),
    AllDataArticleField.OE_NUMBER.field(),
    AllDataArticleField.EAN_NUMBER.field()
  };

  @Override
  public SearchQuery buildQuery(KeywordArticleSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final String normalizedText = defaultNormalisedTextParser().apply(criteria.getText());
    log.debug("Building articles search query with all data of free text = {}",
      normalizedText);

    final MultiMatchQueryBuilder searchQuery =
      QueryBuilders.multiMatchQuery(normalizedText, MULTI_MATCH_FIELDS)
        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
        .operator(Operator.OR);

    log.debug("Used index of All Article Data = {} \nQuery = {}", indices, searchQuery);
    return ArticleQueryUtils.nativeQueryBuilder(pageable, indices).withQuery(searchQuery).build();
  }

  private Function<String, String> defaultNormalisedTextParser() {
    return text -> {
      String normalisedText = QueryUtils.removeNonAlphaChars(
          StringUtils.remove(StringUtils.lowerCase(text), StringUtils.SPACE), true);

      return StringUtils.replace(normalisedText, "/", "//");
    };
  }
}
