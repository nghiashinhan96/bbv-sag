package com.sagag.services.elasticsearch.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "vehId" })
public class VehicleGa implements Serializable {

  private static final long serialVersionUID = -9191455544388309078L;

  @Id
  @JsonIgnore
  private long id;

  @JsonProperty("vehid")
  private String vehId;

}
