package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.article.ArticleCriteriaDto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VehGenArtArticleDto implements Serializable {

  private static final long serialVersionUID = 2488958921431327625L;

  @JsonProperty("artid")
  private String artId;

  @JsonProperty("id_umsart")
  private String idUmsart;

  @JsonProperty("id_pim")
  private String idSagsys;

  @JsonProperty("criteria")
  private List<ArticleCriteriaDto> criteria;

}
