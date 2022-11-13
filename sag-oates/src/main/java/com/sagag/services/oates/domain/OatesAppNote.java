package com.sagag.services.oates.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesAppNote implements Serializable {

  private static final long serialVersionUID = -2704975950715160230L;

  @JsonProperty("@id")
  private String id;

  @JsonProperty("@noteclass")
  private String noteClass;

  @JsonProperty("@noteindex")
  private String noteIndex;

  @JsonProperty("#text")
  private String text;

}
