package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@formatter:off
@JsonPropertyOrder(
    {
      "brandid",
      "pnrn",
      "pnrd",
      "ptype"
      })
//@formatter:on
public class ArticlePart implements Serializable {

  private static final long serialVersionUID = 4382099282300791343L;

  @JsonProperty("brandid")
  private String brandid;

  @JsonProperty("pnrn")
  private String pnrn;

  @JsonProperty("pnrd")
  private String partNumDisplay;

  @JsonProperty("ptype")
  private String ptype;

  @JsonIgnore
  private Map<String, Object> additionalProperties;
}
