package com.sagag.services.elasticsearch.domain.wss;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "language", "text" })
public class WssDesignations implements Serializable {

  private static final long serialVersionUID = 8888625769857108231L;

  @JsonProperty("language")
  private String language;

  @JsonProperty("text")
  private String text;
}
