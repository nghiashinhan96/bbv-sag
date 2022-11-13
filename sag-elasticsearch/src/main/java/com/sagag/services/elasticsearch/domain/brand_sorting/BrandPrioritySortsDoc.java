package com.sagag.services.elasticsearch.domain.brand_sorting;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BrandPrioritySortsDoc implements Serializable {

  private static final long serialVersionUID = -7429489876886985796L;

  @JsonProperty("brand_id")
  private String brandId;

  @JsonProperty("priorities")
  private List<BrandPrioritySortDoc> priorities;

}
