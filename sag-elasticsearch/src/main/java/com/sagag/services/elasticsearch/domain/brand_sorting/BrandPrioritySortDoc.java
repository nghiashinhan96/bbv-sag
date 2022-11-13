package com.sagag.services.elasticsearch.domain.brand_sorting;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class BrandPrioritySortDoc implements Serializable {

  private static final long serialVersionUID = -6116965951121795338L;

  @JsonProperty("affiliate")
  private String affiliate;

  @JsonProperty("collection")
  private String collection;

  @JsonProperty("sort")
  private Map<Integer, Integer> sort;

}
