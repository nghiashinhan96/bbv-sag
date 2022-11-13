package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.dozer.Mapping;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    { 
      "gt_start", 
      "gt_end", 
      "gt_drv", 
      "gt_eng", 
      "gt_mod_alt", 
      "gt_mod", 
      "gt_umc", 
      "gtmod_name", 
    })
public class GTMotiveLink implements Serializable {

  private static final long serialVersionUID = 2684953761893164335L;

  @JsonProperty("gt_start")
  private String gtStart;

  @JsonProperty("gt_end")
  private String gtEnd;

  @JsonProperty("gt_drv")
  private String transmisionCode;

  @JsonProperty("gt_eng")
  private String gtEngineCode;

  @JsonProperty("gt_mod_alt")
  private String gtModAlt;

  @JsonProperty("gt_mod")
  @Mapping("gtModelCode")
  private String gtModelCode;
  
  @JsonProperty("gt_umc")
  private String gtMakeCode;
  
  @JsonProperty("gtmod_name")
  private String gtmodName;

}
