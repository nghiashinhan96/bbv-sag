package com.sagag.services.service.utils.order;

import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.ax.converter.AxPriceTypeConverter;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;

import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;

/**
 * Class to define utility methods for OrderDisplayPrice.
 */
@UtilityClass
public final class OrderDisplayPriceHelper {

  // # 1494
  public static void updateSelectedPrice(ArticleRequest articleRquest, ArticleDocDto article) {
    if (article.hasPrice()) {
      final String defaultPriceTypeByRule = article.getPrice().getPrice().getType();
      Optional.ofNullable(defaultPriceTypeByRule)
          .ifPresent(p -> articleRquest.setPriceDiscTypeId(AxPriceTypeConverter.apply(p)));
    }
    DisplayedPriceDto displayedPrice = article.getDisplayedPrice();
    getArticleBrand(displayedPrice).ifPresent(articleRquest::setBrand);
    getArticleBrandId(displayedPrice).ifPresent(articleRquest::setBrandId);
    getArticleType(displayedPrice)
        .ifPresent(p -> articleRquest.setPriceDiscTypeId(AxPriceTypeConverter.apply(p)));
  }

  private static Optional<String> getArticleBrand(DisplayedPriceDto displayedPrice) {
    if (Objects.isNull(displayedPrice)) {
      return Optional.empty();
    }
    return Optional.ofNullable(displayedPrice.getBrand());
  }

  private static Optional<Long> getArticleBrandId(DisplayedPriceDto displayedPrice) {
    if (Objects.isNull(displayedPrice)) {
      return Optional.empty();
    }
    return Optional.ofNullable(displayedPrice.getBrandId());
  }

  private static Optional<String> getArticleType(DisplayedPriceDto displayedPrice) {
    if (Objects.isNull(displayedPrice)) {
      return Optional.empty();
    }
    return Optional.ofNullable(displayedPrice.getType());
  }

}
