package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    { 
      "id_info", 
      "info_type", 
      "info_txt" 
    })
public class ArticleInfo implements Serializable {

  private static final long serialVersionUID = 8729108408126360969L;

  @JsonProperty("id_info")
  private String idInfo;

  @JsonProperty("info_type")
  private String infoType;

  @JsonProperty("info_txt")
  private String infoTxtDe;

}
