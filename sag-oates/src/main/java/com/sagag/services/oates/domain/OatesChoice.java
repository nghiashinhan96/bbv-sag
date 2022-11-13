package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesChoice implements Serializable {

  private static final long serialVersionUID = -6627925890258235390L;

  private String var;

  @JsonProperty("display_name")
  private String displayName;

  @JsonProperty("option")
  private List<OatesChoiceOption> options;

}
