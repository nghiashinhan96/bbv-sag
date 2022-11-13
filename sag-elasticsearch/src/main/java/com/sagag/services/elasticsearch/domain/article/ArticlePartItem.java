package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    {
      "parts_list_type",
      "sort",
      "gaid",
      "parts_list_item_id_art",
      "parts_list_item_nr_art",
      "quantity",
      "criteria"
    })
//@formatter:on
public class ArticlePartItem implements Serializable {

  private static final long serialVersionUID = 2756660808207896956L;

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
  private List<ArticlePartCriteria> criteria;

}
