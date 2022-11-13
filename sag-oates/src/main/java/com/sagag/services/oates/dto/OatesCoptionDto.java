package com.sagag.services.oates.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesCoptionDto implements Serializable {

  private static final long serialVersionUID = -3453972185361468031L;

  @JsonProperty("conjunction")
  private List<OatesConjunctionDto> conjunction;
}
