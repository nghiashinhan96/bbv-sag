package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenArtTxtDto implements Serializable {

  private static final long serialVersionUID = 5372286361175250509L;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("gatxtdech")
  private String gatxtdech;

}
