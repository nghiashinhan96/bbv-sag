package com.sagag.services.stakis.erp.domain;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import lombok.Data;

@Data
public class TmArticlePriceAndAvailabilityRequest {

  private List<TmBasketPosition> positions;

  public TmArticlePriceAndAvailabilityRequest(List<ArticleDocDto> articles,
      Optional<VehicleDto> vehicle) {
    this.positions = ListUtils.emptyIfNull(articles).stream()
        .map(a -> positionConverter().apply(a, vehicle)).collect(Collectors.toList());
  }

  public TmArticlePriceAndAvailabilityRequest(List<ArticleDocDto> articles) {
    this(articles, Optional.empty());
  }

  private static BiFunction<ArticleDocDto, Optional<VehicleDto>,
    TmBasketPosition> positionConverter() {
    return (article, vehicle) -> {
      final TmBasketPosition position = new TmBasketPosition();
      position.setArticleId(article.getIdSagsys());
      position.setQuantity(article.getAmountNumber());
      vehicle.map(VehicleDto::getKtType).ifPresent(position::setKtType);
      return position;
    };
  }

}
