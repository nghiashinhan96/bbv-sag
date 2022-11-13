package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "filter_sort", "filter_caid" })
public class FilterBar implements Serializable {

  private static final long serialVersionUID = 2672728880580978498L;

  @JsonProperty("filter_sort")
  private String filterSort;

  @JsonProperty("filter_caid")
  private String filterCaid;

  @JsonProperty("filter_default")
  private String filterDefault;

  @JsonProperty("filter_bar")
  private String filterBar;

}
