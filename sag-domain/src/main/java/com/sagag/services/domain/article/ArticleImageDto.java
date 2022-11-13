package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleImageDto implements Serializable {

  private static final long serialVersionUID = -7894486173803650645L;

  @JsonProperty("id_img")
  private Integer imgId;

  @JsonProperty("ref")
  private String href;

  @JsonProperty("sort")
  private Integer sort;
  
  @JsonProperty("img_typ")
  private String imgTyp;

}
