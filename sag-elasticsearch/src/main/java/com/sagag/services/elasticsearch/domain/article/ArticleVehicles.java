package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleUsage;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@Document(
      indexName = "article_vehicles", 
      type = "article", 
      shards = 5, 
      replicas = 1, 
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder({ 
      "id", 
      "vehicles" 
      })
//@formatter:on
public class ArticleVehicles implements Serializable {

  private static final long serialVersionUID = 2488958921431327625L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("article")
  private List<FitmentArticle> article;

  @JsonProperty("vehicles")
  private List<VehicleUsage> vehicles;

}
