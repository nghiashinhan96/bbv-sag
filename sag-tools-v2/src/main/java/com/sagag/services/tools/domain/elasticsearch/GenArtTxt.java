package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "gaid", "gatxtdech" })
public class GenArtTxt implements Serializable {

  private static final long serialVersionUID = -5604947257494195036L;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("gatxtdech")
  private String gatxtdech;
}
