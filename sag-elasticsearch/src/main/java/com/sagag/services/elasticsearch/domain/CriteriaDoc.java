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
      indexName = "criteria", 
      type = "crittxt", 
      shards = 5, 
      replicas = 1, 
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder({ "id", "crittxt" })
public class CriteriaDoc implements Serializable {

  private static final long serialVersionUID = 5496566999003013575L;

  @Id
  @JsonProperty("id")
  private String id;
  
  @JsonProperty("crittxt")
  private List<CriteriaTxt> crittxts;
}
