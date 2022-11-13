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
    { "display_type",
      "gaid",
      "default",
      "ga_de",
      "id_format",
    })
//@formatter:on
public class FormatGaGenarts implements Serializable {

  private static final long serialVersionUID = -7509123369030207070L;

  @JsonProperty("display_type")
  private String displayTtype;

  @JsonProperty("gaid")
  private Integer gaid;

  @JsonProperty("default")
  private String defaultAttr;

  @JsonProperty("ga_de")
  private String generalArtDe;

  @JsonProperty("id_format")
  private Long idFormat;

}
