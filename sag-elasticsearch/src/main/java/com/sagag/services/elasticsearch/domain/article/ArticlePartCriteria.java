package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@formatter:off
@JsonPropertyOrder({
  "cvp",
  "cid",
  "sort",
  "cn"
  })

//@formatter:on
public class ArticlePartCriteria implements Serializable {

  private static final long serialVersionUID = 7607695957531071070L;

  @JsonProperty("cvp")
  private String cvp;

  @JsonProperty("cid")
  private String cid;

  @JsonProperty("sort")
  private int csort;

  @JsonProperty("cn")
  private String cndech;

}
