package com.sagag.services.elasticsearch.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "vegen", "vehid", "genart", "date_create", "date_modify" })
public class VehicleGenArt implements Serializable {

  private static final long serialVersionUID = -1125768878176705424L;

  @JsonProperty("vegen")
  private String vegen;

  @JsonProperty("vehid")
  private String vehId;

  @JsonProperty("genart")
  private Integer genart;

  @JsonProperty("date_create")
  private Integer dateCreate;

  @JsonProperty("date_modify")
  private Integer dateModify;
}
