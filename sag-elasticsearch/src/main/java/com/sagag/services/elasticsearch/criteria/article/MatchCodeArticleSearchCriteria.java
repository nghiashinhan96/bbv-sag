package com.sagag.services.elasticsearch.criteria.article;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MatchCodeArticleSearchCriteria extends ArticleAggregateCriteria {

  private String matchCode;

}
