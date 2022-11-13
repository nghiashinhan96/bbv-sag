package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
//@formatter:off
@JsonPropertyOrder({
  "filter_default",
  "filter_open",
  "filter_bar",
  "filter_uom",
  "filter_caid",
  "filter_sort"
})
//@formatter:on
public class UnitreeBarFilter implements Serializable {

  private static final long serialVersionUID = 2917822705144349991L;

  @JsonProperty("filter_default")
  private String filterDefault;

  @JsonProperty("filter_open")
  private String filterOpen;

  @JsonProperty("filter_bar")
  private String filterBar;

  @JsonProperty("filter_uom")
  private String filterUom;

  @JsonProperty("filter_caid")
  private String filterCaid;

  @JsonProperty("filter_sort")
  private String filterSort;

}
