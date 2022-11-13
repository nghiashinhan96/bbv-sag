package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OatesApplicationRefItem implements Serializable {

  private static final long serialVersionUID = -1128406963787209581L;

  @JsonProperty("")
  private String name;

  @JsonProperty("fueltype")
  private String fuelType;

  @JsonProperty("display_name")
  private String displayName;

  @JsonProperty("decision_tree")
  private OatesDecisionTree decisionTree;

  @JsonProperty("application_ids")
  private List<String> applicationIds;

}
