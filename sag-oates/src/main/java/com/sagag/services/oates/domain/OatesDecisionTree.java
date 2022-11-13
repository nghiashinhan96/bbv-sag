package com.sagag.services.oates.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesDecisionTree implements Serializable {

  private static final long serialVersionUID = -5501119192375753512L;

  private String type;

  @JsonProperty("application_id")
  private String applicationId;

  private OatesChoice choice;

}
