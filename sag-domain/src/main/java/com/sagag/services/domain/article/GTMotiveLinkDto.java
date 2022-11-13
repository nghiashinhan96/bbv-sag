package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GTMotiveLinkDto implements Serializable {

  private static final long serialVersionUID = -6591398590381411815L;

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

}
