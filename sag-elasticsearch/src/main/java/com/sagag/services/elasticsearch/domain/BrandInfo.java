package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder({ 
      "brand", 
      "prio" 
      })
//@formatter:on
public class BrandInfo implements Serializable {

  private static final long serialVersionUID = 3754710962967980453L;

  @JsonProperty("brand")
  private Integer brand;

  @JsonProperty("prio")
  private String prio;
}
