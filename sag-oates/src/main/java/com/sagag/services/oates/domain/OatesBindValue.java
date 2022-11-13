package com.sagag.services.oates.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesBindValue implements Serializable {

  private static final long serialVersionUID = 2175351419717394332L;

  @JsonProperty("@display_name")
  private String displayName;

  @JsonProperty("@display_value")
  private String displayValue;

  @JsonProperty("@value")
  private String value;

}
