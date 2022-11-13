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
      indexName = "suppliers", 
      type = "supp_txt", 
      shards = 5, 
      replicas = 1, 
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder(
    { 
      "id", 
      "supp_txt"
    })
//@formatter:on
public class SupplierDoc implements Serializable {

  private static final long serialVersionUID = 5085237077992004564L;
  
  @Id
  @JsonProperty("id")
  private String id;
  
  @JsonProperty("supp_txt")
  private List<SupplierTxt> suppTxts;
}
