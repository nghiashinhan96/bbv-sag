package com.sagag.services.elasticsearch.criteria.article;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartReferenceAndGaIdListSearchCriteria extends ArticleAggregateCriteria {

  private List<String> prnrs;

  private List<String> gaIds;

}
