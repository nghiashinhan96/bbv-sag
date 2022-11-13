package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesChangeInterval implements Serializable {

  private static final long serialVersionUID = -1983383464872340035L;

  @JsonProperty("@application_id")
  private String applicationId;

  @JsonProperty("@application_name")
  private String applicationName;

  @JsonProperty("interval")
  private List<OatesInterval> intervals;

}
