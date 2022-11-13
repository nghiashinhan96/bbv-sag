package com.sagag.services.elasticsearch.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder(
    {
      "artid",
      "id_umsart",
      "id_pim",
      "criteria"
    })
//@formatter:on
public class FitmentArticle implements Serializable {

  private static final long serialVersionUID = 2488958921431327625L;

  @JsonProperty("artid")
  private String artId;

  @JsonProperty("id_umsart")
  private String idUmsart;

  @JsonProperty("id_pim")
  private String idSagsys;

  @JsonProperty("id_autonet")
  private String idAutonet;

  @JsonProperty("criteria")
  @Field(type = FieldType.Nested)
  private List<ArticleCriteria> criteria;

}
