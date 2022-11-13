package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.elasticsearch.domain.GenArtTxt;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
// @formatter:off
@Document(
      indexName = "ax_articles_de",
      type = "refs",
      shards = 5,
      replicas = 1,
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder(
    {
      "id",
      "id_source",
      "artnr",
      "id_umsart",
      "is100pr",
      "gaID",
      "genArtTxts",
      "id_pim",
      "id_dlnr",
      "supplier",
      "id_autonet",
      "artid",
      "artnr_display",
      "partDesc",
      "amountNumber",
      "salesQuantity",
      "isPromotion",
      "available",
      "product_designation_addon",
      "article",
      "product_brand",
      "id_product_brand",
      "name",
      "product_addon",
      "sag_product_group",
      "sag_product_group_2",
      "sag_product_group_3",
      "sag_product_group_4",
      "icat",
      "icat2",
      "icat3",
      "icat4",
      "icat5",
      "criteria",
      "images",
      "info",
      "tyre_article",
      "qty_standard_ch",
      "qty_lowest_ch",
      "qty_multiple_ch",
      "qty_standard_at",
      "qty_lowest_at",
      "qty_multiple_at",
      "qty_standard_be",
      "qty_lowest_be",
      "qty_multiple_be",
      "qtyMultiple",
      "qty_multiple",
      "parts_ext"
    })
//@formatter:on
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

  @JsonProperty("id_pim")
  private String idSagsys;

  @JsonProperty("id_autonet")
  private String idAutonet;

  @JsonProperty("is_100pr")
  private boolean is100pr;

  @JsonProperty("id_dlnr")
  private String idDlnr;

  @JsonProperty("supplier")
  private String supplier;

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

  @JsonProperty("article")
  private FitmentArticle article;

  @JsonProperty("product_brand")
  private String productBrand;

  @JsonProperty("id_product_brand")
  private String idProductBrand;

  @JsonProperty("name")
  private String name;

  @JsonProperty("product_addon")
  private String productAddon;

  @JsonProperty("sag_product_group")
  private String sagProductGroup;

  @JsonProperty("sag_product_group_2")
  private String sagProductGroup2;

  @JsonProperty("sag_product_group_3")
  private String sagProductGroup3;

  @JsonProperty("sag_product_group_4")
  private String sagProductGroup4;

  @JsonProperty("icat")
  private String icat;

  @JsonProperty("icat2")
  private String icat2;

  @JsonProperty("icat3")
  private String icat3;

  @JsonProperty("icat4")
  private String icat4;

  @JsonProperty("icat5")
  private String icat5;

  @JsonProperty("qty_standard_ch")
  private Integer qtyStandardCh;

  @JsonProperty("qty_lowest_ch")
  private Integer qtyLowestCh;

  @JsonProperty("qty_multiple_ch")
  private Integer qtyMultipleCh;

  @JsonProperty("qty_standard_at")
  private Integer qtyStandardAt;

  @JsonProperty("qty_lowest_at")
  private Integer qtyLowestAt;

  @JsonProperty("qty_multiple_at")
  private Integer qtyMultipleAt;

  @JsonProperty("qty_standard_be")
  private Integer qtyStandardBe;

  @JsonProperty("qty_lowest_be")
  private Integer qtyLowestBe;

  @JsonProperty("qty_multiple_be")
  private Integer qtyMultipleBe;

  @JsonProperty("qty_standard_cz")
  private Integer qtyStandardCz;

  @JsonProperty("qty_lowest_cz")
  private Integer qtyLowestCz;

  @JsonProperty("qty_multiple_cz")
  private Integer qtyMultipleCz;

  @JsonProperty("qty_multiple")
  private Integer qtyMultiple;

  @JsonProperty("qty_standard")
  private Integer qtyStandard;

  @JsonProperty("qty_lowest")
  private Integer qtyLowest;

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

  @JsonProperty("locks")
  private String locks;

  @JsonProperty("has_replacement")
  private String hasReplacement;

  @JsonProperty("is_replacement_for")
  private String isReplacementFor;

  @JsonProperty("parts_ext")
  @Field(type = FieldType.Nested)
  private List<ArticlePart> partsExt;

  @JsonProperty("locks_vk")
  private String locksVk;

  @JsonProperty("locks_ku")
  private String locksKu;

  @JsonProperty("accessory_lists")
  @Field(type = FieldType.Nested)
  private List<ArticleAccessory> accessoryLists;

  @JsonProperty("parts_list_items")
  private List<ArticlePartItem> partListItems;

  @JsonIgnore
  private boolean exactMatched;

  private boolean bom;
  
  private boolean pseudo;

  private RelevanceGroupType relevanceGroupType;
  
}
