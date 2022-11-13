package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.offer.ShopArticle;
import com.sagag.eshop.service.dto.offer.ShopArticleDto;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 *
 */
@UtilityClass
public final class ShopArticleConverters {

  public static ShopArticleDto convert(ShopArticle entity) {
    return new ShopArticleDto(entity);
  }

  public static Converter<ShopArticle, ShopArticleDto> pageShopArticleConverter() {
    return ShopArticleConverters::convert;
  }

  public static Function<ShopArticle, ShopArticleDto> optionalShopArticleConverter() {
    return ShopArticleConverters::convert;
  }

}
