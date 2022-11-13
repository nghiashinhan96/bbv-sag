package com.sagag.services.elasticsearch.domain.formatga;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
      indexName = "format_ga", 
      type = "genarts", 
      shards = 5, 
      replicas = 1, 
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder({ 
      "elements",
      "genarts" 
  })
//@formatter:on
public class FormatGaDoc implements Serializable {

  private static final long serialVersionUID = 4812249544804935650L;

  @Id
  private long id;

  @JsonProperty("genarts")
  private List<FormatGaGenarts> genarts;

  @JsonProperty("elements")
  @Field(type = FieldType.Nested)
  private List<FormatGaElement> elements;


}
