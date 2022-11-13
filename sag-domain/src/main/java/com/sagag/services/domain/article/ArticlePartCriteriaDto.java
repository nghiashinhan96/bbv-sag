package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticlePartCriteriaDto implements Serializable {

  private static final long serialVersionUID = 6570762250474375517L;

  @JsonProperty("cvp")
  private String cvp;

  @JsonProperty("cid")
  private String cid;

  @JsonProperty("cn")
  private String cndech;

  @JsonProperty("sort")
  private int csort;

}
