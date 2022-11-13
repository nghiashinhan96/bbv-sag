package com.sagag.services.oates.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesBind implements Serializable {

  private static final long serialVersionUID = -1821511534349184627L;

  @JsonProperty("var")
  private String var;

  @JsonProperty("value")
  private OatesBindValue value;

}
