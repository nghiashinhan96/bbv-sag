package com.sagag.services.elasticsearch.domain.formatga;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    { "postfix",
      "cid",
      "delimiter",
      "ctxt_de",
      "show_criteria",
      "format",
      "alternate",
      "prefix",
      "sort",
      "order"
    })
//@formatter:on
public class FormatGaElement implements Serializable {

  private static final long serialVersionUID = 7577674986333966112L;

  @JsonProperty("postfix")
  private String postfix;

  @JsonProperty("cid")
  private Integer cid;

  @JsonProperty("delimiter")
  private String delimiter;

  @JsonProperty("ctxt_de")
  private String ctxtDe;

  @JsonProperty("show_criteria")
  private String showCriteria;

  @JsonProperty("format")
  private String format;

  @JsonProperty("alternate")
  private String alternate;

  @JsonProperty("prefix")
  private String prefix;

  @JsonProperty("sort")
  private String sort;

  @JsonProperty("order")
  private String order;

}
