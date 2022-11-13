package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@Document(
      indexName = "categories",
      type = "cat_tree",
      shards = 5,
      replicas = 1,
      refreshInterval = "-1",
      createIndex = false,
      useServerConfiguration = true
    )
@JsonPropertyOrder(
    {
      "leafid",
      "parentid",
      "leaftxt",
      "sag_code",
      "sort",
      "id_dlnr",
      "id_product_brand",
      "service",
      "open",
      "qcol",
      "qrow",
      "qflag",
      "qlevel",
      "qsort",
      "qshow",
      "classic_col",
      "is_check",
      "link",
      "genarts"
    })
//@formatter:on
@EqualsAndHashCode(of = "id")
public class CategoryDoc implements Serializable {

  private static final long serialVersionUID = -5579092860273915613L;

  @Id
  @JsonProperty("id")
  private String id;

  @JsonProperty("genarts")
  @Field(type = FieldType.Nested)
  private List<GenArt> genarts;

  @JsonProperty("leafid")
  private String leafid;

  @JsonProperty("parentid")
  private String parentid;

  @JsonProperty("leaftxt")
  private String leaftxt;

  @JsonProperty("sag_code")
  private String sagCode;

  @JsonProperty("sort")
  private String sort;

  @JsonProperty("id_dlnr")
  private String idDlnr;

  @JsonProperty("id_product_brand")
  private String idProductBrand;

  @JsonProperty("service")
  private String service;

  @JsonProperty("open")
  private String open;

  @JsonProperty("qcol")
  private String qcol;

  @JsonProperty("qrow")
  private String qrow;

  @JsonProperty("node_keywords")
  private String nodeKeywords;

  @JsonProperty("qflag")
  private String qflag;

  @JsonProperty("qlevel")
  private String qlevel;

  @JsonProperty("qsort")
  private String qsort;

  @JsonProperty("qshow")
  private String qshow;

  @JsonProperty("classic_col")
  private String classicCol;

  @JsonProperty("is_check")
  private String isCheck;

  @JsonProperty("link")
  private String link;

  @JsonProperty("external_service")
  private String externalService;

  @JsonProperty("external_service_attribute")
  private String externalServiceAttribute;

  @JsonProperty("qfold")
  private String qfold;

  @JsonProperty("qfold_show")
  private String qfoldShow;

  @JsonProperty("vc")
  private String vehicleClass;

}
