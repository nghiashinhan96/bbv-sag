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
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePartItemDto implements Serializable {

  private static final long serialVersionUID = 151196495881903084L;

  @JsonProperty("parts_list_type")
  private String partsListType;

  @JsonProperty("sort")
  private Integer sort;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("parts_list_item_id_art")
  private String partsListItemIdArt;

  @JsonProperty("parts_list_item_nr_art")
  private String partsListItemNrArt;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("criteria")
  private List<ArticlePartCriteriaDto> criteria;

}
