package com.sagag.services.elasticsearch.criteria.article;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class TyreArticleSearchCriteria extends BaseTyreArticleSearchCriteria {

  private Set<String> seasonGenArtIds;
  private Set<String> fzCategoryGenArtIds;

}
