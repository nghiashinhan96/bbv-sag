package com.sagag.services.elasticsearch.domain.wss;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
//@formatter:off
@Document(
   indexName = "wss_artgrp_ch",
   type = "artgrp_tree",
   shards = 5,
   replicas = 1,
   refreshInterval = "-1",
   createIndex = false,
   useServerConfiguration = true
 )
@JsonPropertyOrder({ "id", "artgrp_tree", "designations" })
public class WssArticleGroupDoc {

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("artgrp_tree")
  @Field(type = FieldType.Nested)
  private List<WssArtGrpTree> wssArtGrpTree;

  @JsonProperty("designations")
  @Field(type = FieldType.Nested)
  private List<WssDesignations> wssDesignations;
}
