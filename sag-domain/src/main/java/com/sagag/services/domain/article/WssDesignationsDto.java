package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "language", "text" })
@NoArgsConstructor
@AllArgsConstructor
public class WssDesignationsDto implements Serializable {

  private static final long serialVersionUID = 8888625769857108231L;

  @JsonProperty("language")
  private String language;

  @JsonProperty("text")
  private String text;
}
