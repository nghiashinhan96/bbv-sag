package com.sagag.eshop.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasketHistoryItemDto implements Serializable {

  private static final long serialVersionUID = 8901165460599813425L;

  private List<ArticleDocDto> articles;

  private VehicleDto vehicle;

  public BasketHistoryItemDto(VehicleDto vehicle, List<ArticleDocDto> articles) {
    this.articles = articles;
    this.vehicle = vehicle;
  }

}
