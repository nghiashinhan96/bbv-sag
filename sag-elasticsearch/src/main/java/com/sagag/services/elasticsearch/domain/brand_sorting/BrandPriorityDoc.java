package com.sagag.services.elasticsearch.domain.brand_sorting;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document(
    indexName = "brand_priority",
    type = "brand_prio",
    shards = 5,
    replicas = 1,
    refreshInterval = "-1",
    createIndex = false,
    useServerConfiguration = true
  )
public class BrandPriorityDoc implements Serializable {

  private static final long serialVersionUID = -6507895241584150600L;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("sort_type")
  private String sortType;

  @JsonProperty("sorts")
  private List<BrandPrioritySortsDoc> sorts;

  @JsonProperty("brands")
  private List<BrandPrioritySortsDoc> brands;

}
