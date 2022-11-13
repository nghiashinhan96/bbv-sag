package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesCoption implements Serializable {

  private static final long serialVersionUID = -3453972185361468031L;

  @JsonProperty("conjunction")
  private List<OatesConjunction> conjunction;
}
