package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "gaid" })
public class GenArts implements Serializable {

  private static final long serialVersionUID = -6442509612599751755L;

  @JsonProperty("gaid")
  private String gaid;

}
