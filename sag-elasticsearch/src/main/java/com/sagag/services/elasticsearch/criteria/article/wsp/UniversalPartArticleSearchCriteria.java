package com.sagag.services.elasticsearch.criteria.article.wsp;

import com.sagag.services.elasticsearch.criteria.article.ArticleAggregateCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Builder
public class UniversalPartArticleSearchCriteria extends ArticleAggregateCriteria {

  private UniversalPartArticleSearchTerm includeTerm;

  private UniversalPartArticleSearchTerm excludeTerm;

  private String language;

}
