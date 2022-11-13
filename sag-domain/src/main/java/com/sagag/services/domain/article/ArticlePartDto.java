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
public class ArticlePartDto implements Serializable {

  private static final long serialVersionUID = -5373207728521040216L;

  @JsonProperty("brandid")
  private String brandid;

  @JsonProperty("pnrn")
  private String pnrn;

  @JsonProperty("pnrd")
  private String partNumDisplay;

  @JsonProperty("ptype")
  private String ptype;

}
