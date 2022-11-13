package com.sagag.services.elasticsearch.criteria.article;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.util.List;

@Data
public class ArticleAggregateCriteria {

  private List<String> gaIds;

  private List<String> supplierRaws;

  private List<String> cids;

  private List<ArticleAggregateMultiLevel> aggregateMultiLevels;
  
  private String idDlnr;

  private boolean aggregated;

  private boolean needSubAggregated;

  private boolean useMultipleAggregation;

  private boolean isSaleOnBehalf;

  @JsonIgnore
  private String[] affNameLocks;

  public void onAggregated() {
    this.aggregated = true;
  }
}
