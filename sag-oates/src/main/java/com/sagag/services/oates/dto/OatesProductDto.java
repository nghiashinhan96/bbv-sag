package com.sagag.services.oates.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesProductDto implements Serializable {

  private static final long serialVersionUID = 1163807483428376518L;

  private String type;

  private String name;

  private String tier;

  @JsonProperty("resource")
  private List<OatesResourceDto> resources;
}
