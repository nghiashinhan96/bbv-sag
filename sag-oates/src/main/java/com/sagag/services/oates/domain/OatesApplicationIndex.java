package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesApplicationIndex implements Serializable {

  private static final long serialVersionUID = -9187404088056684995L;

  @JsonProperty("app_ref")
  private List<OatesApplicationRefItem> appRef;

}
