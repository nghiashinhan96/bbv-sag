package com.sagag.services.tools.domain.elasticsearch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GtMotiveLink implements Serializable {

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
  private String gtModelCode;
  
  @JsonProperty("gt_umc")
  private String gtMakeCode;
  
  @JsonProperty("gtmod_name")
  private String gtmodName;
  
  @JsonProperty("gt_equip")
  private String gtEquip;

}
