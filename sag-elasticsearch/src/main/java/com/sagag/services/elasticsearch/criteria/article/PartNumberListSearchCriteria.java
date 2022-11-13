package com.sagag.services.elasticsearch.criteria.article;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PartNumberListSearchCriteria extends ArticleAggregateCriteria {

  @NonNull
  private String[] partNumbers;

  @NonNull
  private String supplier;

  @NonNull
  private String[] affNameLocks;

}
