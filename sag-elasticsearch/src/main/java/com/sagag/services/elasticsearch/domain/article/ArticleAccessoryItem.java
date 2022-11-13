package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
//@formatter:off
@JsonPropertyOrder(
    {
      "sort",
      "accessory_article_id_art",
      "accessory_article_id_manufacturer",
      "gaid",
      "accessory_article_art_nr",
      "quantity"
      })
//@formatter:on
public class ArticleAccessoryItem implements Serializable {

  private static final long serialVersionUID = -7425512465986306732L;

  @JsonProperty("sort")
  private Integer sort;

  @JsonProperty("accessory_article_id_art")
  private String accessoryArticleIdArt;

  @JsonProperty("accessory_article_id_manufacturer")
  private String accessoryArticleIdManufacturer;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("accessory_article_art_nr")
  private String accesoryArticleArtNr;

  @JsonProperty("quantity")
  private Integer quantity;

  @JsonProperty("criteria")
  @Field(type = FieldType.Nested)
  private List<ArticleAccessoryCriteria> criteria;

}
