package com.sagag.services.oates.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesInterval implements Serializable {

  private static final long serialVersionUID = -1782243106586313613L;

  @JsonProperty("@display_name")
  private String displayName;

  @JsonProperty("@display_unit")
  private String displayUnit;

  @JsonProperty("@unit")
  private String unit;

  @JsonProperty("#text")
  private String text;

}
