package com.sagag.services.elasticsearch.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.elasticsearch.domain.article.FitmentArticle;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@Document(
      indexName = "ax_vehicle_genart_art_de", 
      type = "vga", 
      shards = 5, 
      replicas = 1, 
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder({ 
      "id", 
      "vga", 
      "articles" 
      })
//@formatter:on
public class VehicleGenArtArtDoc implements Serializable {

  private static final long serialVersionUID = 2488958921431327625L;

  @Id
  @JsonProperty("id")
  private String id;

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

  @JsonProperty("vga")
  private List<VehicleGenArt> vgas = new ArrayList<>();

  @JsonProperty("articles")
  @Field(type = FieldType.Nested)
  private List<FitmentArticle> articles;

}
