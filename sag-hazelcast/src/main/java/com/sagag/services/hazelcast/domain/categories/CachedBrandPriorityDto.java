package com.sagag.services.hazelcast.domain.categories;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.eshop.category.BrandDto;
import com.sagag.services.domain.eshop.category.SortDto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CachedBrandPriorityDto implements Serializable {

  private static final long serialVersionUID = -5354653501784979999L;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("sort_type")
  private String sortType;

  @JsonProperty("sorts")
  private List<SortDto> sorts;

  @JsonProperty("brands")
  private List<BrandDto> brands;

}
