package com.sagag.services.elasticsearch.criteria.wss;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.elasticsearch.search.sort.SortOrder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WssArticleGroupSearchSortCriteria {
  @JsonProperty("artgrp")
  private SortOrder articleGroup;
}
