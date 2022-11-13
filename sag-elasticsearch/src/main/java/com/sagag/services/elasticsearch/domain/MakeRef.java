package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    { "id_make",
      "make",
      "gtmake",
      "normauto",
      "gtvin"
    })
//@formatter:on
public class MakeRef implements Serializable {

  private static final long serialVersionUID = 6804286720993722090L;

  @JsonProperty("id_make")
  private Integer idMake;

  @JsonProperty("make")
  private String make;

  @JsonProperty("gtmake")
  private String gtmake;

  @JsonProperty("normauto")
  private boolean normauto;

  @JsonProperty("gtvin")
  private boolean gtvin;

  @JsonProperty("vt")
  private String vehicleType;

  @JsonProperty("top")
  private String top;

  @JsonProperty("vehicle_class")
  private String vehicleClass;

  @JsonProperty("vehicle_sub_class")
  private String vehicleSubClass;

}
