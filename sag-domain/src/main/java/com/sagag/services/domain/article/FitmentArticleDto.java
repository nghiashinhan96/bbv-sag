package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "artid", "id_umsart", "id_pim", "criteria" })
public class FitmentArticleDto implements Serializable {

  private static final long serialVersionUID = 7578294702304849414L;

  @JsonProperty("artid")
  private String artId;

  @JsonProperty("id_umsart")
  private String idUmsart;

  @JsonProperty("id_pim")
  private String idSagsys;

  @JsonProperty("genart")
  private Integer genart;

  @JsonProperty("criteria")
  private List<ArticleCriteriaDto> criteria;
}
