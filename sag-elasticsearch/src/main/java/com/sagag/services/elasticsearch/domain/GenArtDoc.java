package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@Document(
      indexName = "genart", 
      type = "genart_txt", 
      shards = 5, 
      replicas = 1, 
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder(
    { 
      "id", 
      "genart_txt"
    })
//@formatter:on
public class GenArtDoc implements Serializable {

  private static final long serialVersionUID = 5657462185128436620L;

  @Id
  @JsonProperty("id")
  private String id;
  
  @JsonProperty("genart_txt")
  private List<GenArtTxt> genArtTxts;
}
