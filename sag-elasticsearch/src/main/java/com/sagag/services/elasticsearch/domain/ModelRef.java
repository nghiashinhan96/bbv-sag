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
    { "id_model",
      "model",
      "sort",
      "model_date_begin",
      "model_date_end",
      "id_make",
      "date_create",
      "date_modify"
    })
//@formatter:on
public class ModelRef implements Serializable {

  private static final long serialVersionUID = -5121288037846009095L;

  @JsonProperty("id_model")
  private Integer idModel;

  @JsonProperty("model")
  private String model;

  @JsonProperty("sort")
  private Integer sort;

  @JsonProperty("model_date_begin")
  private Integer modelDateBegin;

  @JsonProperty("model_date_end")
  private Integer modelDateEnd;

  @JsonProperty("id_make")
  private Integer idMake;

  @JsonProperty("vehicle_class")
  private String vehicleClass;

  @JsonProperty("vehicle_sub_class")
  private String vehicleSubClass;

  @JsonProperty("model_series")
  private String modelSeries;

  @JsonProperty("model_gen")
  private String modelGen;
}
