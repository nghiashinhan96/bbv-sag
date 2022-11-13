package com.sagag.services.oates.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesCapacityDto implements Serializable {

  private static final long serialVersionUID = -4959454594077187819L;

  @JsonProperty("@type")
  private String type;

  private String option;

}
