package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAccessoryItemDto implements Serializable {

  private static final long serialVersionUID = -8792245017789228283L;
  
  @JsonProperty("sort")
  private Integer sort;

  @JsonProperty("accessoryArticleIdArt")
  private String accessoryArticleIdArt;

  @JsonProperty("accessoryArticleIdManufacturer")
  private String accessoryArticleIdManufacturer;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("accesoryArticleArtNr")
  private String accesoryArticleArtNr;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("criteria")
  private List<ArticleAccessoryCriteriaDto> criteria;
}
