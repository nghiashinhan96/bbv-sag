package com.sagag.services.oates.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesNoteRef implements Serializable {

  private static final long serialVersionUID = -5672278672229563283L;

  @JsonProperty("@id")
  private String id;

  @JsonProperty("@noteclass")
  private String noteClass;

  @JsonProperty("@noteindex")
  private String noteIndex;

}
