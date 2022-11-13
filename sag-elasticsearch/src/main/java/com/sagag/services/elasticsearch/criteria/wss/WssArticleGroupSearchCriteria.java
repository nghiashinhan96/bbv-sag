package com.sagag.services.elasticsearch.criteria.wss;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

/**
 * WSS Article Group search criteria class.
 */
@Data
@Builder
public class WssArticleGroupSearchCriteria {

  @JsonProperty("term")
  private WssArticlegroupSearchTermCriteria searchTerm;

  @JsonProperty("filtering")
  private WssArticleGroupFilteringCriteria filtering;

  @JsonProperty("sort")
  private WssArticleGroupSearchSortCriteria sort;
}
