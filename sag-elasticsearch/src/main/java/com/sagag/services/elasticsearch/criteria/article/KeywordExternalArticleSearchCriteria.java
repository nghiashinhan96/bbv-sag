package com.sagag.services.elasticsearch.criteria.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordExternalArticleSearchCriteria extends KeywordArticleSearchCriteria {

  private SearchQuery[] searchQueries;

  private boolean fromTextSearch;

  private boolean disableIgnoreGenArtMatch;

}
