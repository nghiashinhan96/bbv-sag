package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@Document(
      indexName = "articles_de_agg",
      type = "refs",
      shards = 5,
      replicas = 1,
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
public class ArticleDoc implements Serializable {

  private static final long serialVersionUID = 2488958921431327625L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("id_source")
  private String idSource;

  @JsonProperty("artnr")
  private String artnr;

  @JsonProperty("id_umsart")
  private String idUmsart;

  @JsonProperty("gaID")
  private String gaId;

  @JsonProperty("genArtTxts")
  private List<GenArtTxt> genArtTxts;

  @JsonProperty("id_sagsys")
  private String idSagsys;

  @JsonProperty("id_dlnr")
  private String idDlnr;

  @JsonProperty("supplier")
  private String supplier;

  @JsonProperty("id_autonet")
  private String idAutonet;

  @JsonProperty("artid")
  private String artid;

  @JsonProperty("artnr_display")
  private String artnrDisplay;

  @JsonProperty("partDesc")
  private String partDesc;

  @JsonProperty("amountNumber")
  private Integer amountNumber;

  @JsonProperty("salesQuantity")
  private Integer salesQuantity;

  @JsonProperty("isPromotion")
  private String isPromotion;

  @JsonProperty("available")
  private boolean available;

  @JsonProperty("product_designation_addon")
  private String productDesignationAddon;

  @JsonProperty("stock")
  private ArticleStock stock;

  @JsonProperty("availability")
  private Availability availability;

  @JsonProperty("article")
  private Article article;

  @JsonProperty("product_brand")
  private String productBrand;

  @JsonProperty("id_product_brand")
  private String idProductBrand;

  @JsonProperty("name")
  private String name;

  @JsonProperty("price")
  private PriceWithArticle price;

  @JsonProperty("product_addon")
  private String productAddon;

  @JsonProperty("sag_product_group")
  private String sagProductGroup;

  @JsonProperty("criteria")
  @Field(type = FieldType.Nested)
  private List<ArticleCriteria> criteria;

  @JsonProperty("images")
  private List<ArticleImage> images;

  @JsonProperty("parts")
  @Field(type = FieldType.Nested)
  private List<ArticlePart> parts;

  @JsonProperty("info")
  @Field(type = FieldType.Nested)
  private List<ArticleInfo> infos;

  @JsonProperty("tyre_article")
  private boolean tyreArticle;

  @JsonIgnore
  private Map<String, Object> additionalProperties;

  @JsonProperty("locks")
  private String locks;

  @JsonProperty("locks_front_end")
  private String locksFrontEnd;

}
