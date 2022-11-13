package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
//@formatter:off
@JsonPropertyOrder({
  "gaid",
  "filter"
})
//@formatter:on
public class Filter implements Serializable {

  private static final long serialVersionUID = 4244695049847493511L;

  @JsonProperty("gaid")
  private String gaid;
  
  @JsonProperty("filter")
  private List<UnitreeBarFilter> filter;

}
