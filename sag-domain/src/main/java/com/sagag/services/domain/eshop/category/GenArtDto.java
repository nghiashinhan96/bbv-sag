package com.sagag.services.domain.eshop.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.common.domain.CriteriaDto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "gaid", "criteria", "brands", "cupis", "sort" })
public class GenArtDto implements Serializable {

  private static final long serialVersionUID = -46153690747677260L;

  @JsonIgnore
  private long id;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("criteria")
  private List<CriteriaDto> criteria;

  @JsonProperty("brands")
  private List<BrandDto> brands;

  @JsonProperty("cupis")
  private List<GtCUPIDto> cupis;

  @JsonProperty("sorts")
  private List<SortDto> sorts;

  @JsonProperty("filters")
  private List<FilterBarDto> filters;
}
