package com.sagag.services.elasticsearch.criteria.article;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class MotorTyreArticleSearchCriteria extends BaseTyreArticleSearchCriteria {

  private Set<String> categoryGenArtIds;

}
