package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesConjunction implements Serializable {

  private static final long serialVersionUID = -5750825928088131175L;

  @JsonProperty("bind")
  private List<OatesBind> bind;

}
