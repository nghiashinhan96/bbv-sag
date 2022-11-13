package com.sagag.services.elasticsearch.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.elasticsearch.domain.GenArts;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@Document(
      indexName = "vehicle_genart", 
      type = "vehicle_ga", 
      shards = 5, 
      replicas = 1, 
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder(
    { 
      "vehicle_ga",
      "gen_arts"
    })
//@formatter:on
public class VehicleGenArtDoc implements Serializable {

  private static final long serialVersionUID = 1271938649153697149L;

  @Id
  @JsonIgnore
  private long id;

  @JsonProperty("vehicle_ga")
  private List<VehicleGa> vehicleGas;
  
  @JsonProperty("gen_arts")
  @Field(type = FieldType.Nested)
  private List<GenArts> genArts;

}
