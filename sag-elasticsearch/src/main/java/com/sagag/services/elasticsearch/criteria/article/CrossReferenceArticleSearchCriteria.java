package com.sagag.services.elasticsearch.criteria.article;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CrossReferenceArticleSearchCriteria extends ArticleAggregateCriteria {

  private String artNr;
  private String brandId;
}
