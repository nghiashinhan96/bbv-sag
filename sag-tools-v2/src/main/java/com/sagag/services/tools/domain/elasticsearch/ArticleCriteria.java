package com.sagag.services.tools.domain.elasticsearch;

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
  "csort",
  "cn"
  })

//@formatter:on
public class ArticleCriteria implements Serializable {

  private static final long serialVersionUID = 2433254986309938724L;

  @JsonProperty("cvp")
  private String cvp;

  @JsonProperty("cid")
  private String cid;

  @JsonProperty("csort")
  private int csort;

  @JsonProperty("cn")
  private String cndech;

}
