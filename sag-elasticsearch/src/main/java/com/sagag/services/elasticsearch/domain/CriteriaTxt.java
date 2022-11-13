package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "cid", "cndech" })
public class CriteriaTxt implements Serializable {

  private static final long serialVersionUID = -1354124249547762132L;

  @JsonProperty("cid")
  private String cid;

  @JsonProperty("cndech")
  private String cndech;
}
