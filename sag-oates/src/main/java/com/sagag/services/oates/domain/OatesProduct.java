package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesProduct implements Serializable {

  private static final long serialVersionUID = 1163807483428376518L;

  @JsonProperty("@type")
  private String type;

  private String name;

  @JsonProperty("@tier")
  private String tier;

  @JsonProperty("resource")
  private List<OatesResource> resources;
}
