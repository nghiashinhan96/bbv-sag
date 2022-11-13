package com.sagag.services.oates.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesChoiceDto implements Serializable {

  private static final long serialVersionUID = -6627925890258235390L;

  private String var;

  private String displayName;

  @JsonProperty("option")
  private List<OatesChoiceOptionDto> options;

}
