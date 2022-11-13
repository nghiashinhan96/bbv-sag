package com.sagag.services.oates.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OatesChoiceOptionDto implements Serializable {

  private static final long serialVersionUID = 1760536233204032532L;

  private String value;

  @JsonProperty("display_value")
  private String displayValue;

  private String type;

  @JsonProperty("application_id")
  private String applicationId;

  @JsonProperty("choice")
  private OatesChoiceDto choice;

  private String guid;

  private String cateId;

  private String cateName;

}
