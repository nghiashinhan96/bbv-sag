package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@formatter:off
@JsonPropertyOrder(
    {
      "sort",
      "id_img",
      "ref",
      "img_typ"
      })
//@formatter:on
public class ArticleImage implements Serializable {

  private static final long serialVersionUID = 4382099282300791343L;

  @JsonProperty("sort")
  private Integer sort;

  @JsonProperty("id_img")
  private Integer imgId;

  @JsonProperty("ref")
  private String href;

  @JsonProperty("img_typ")
  private String imgTyp;

}
