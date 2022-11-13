package com.sagag.services.rest.resource.offer;

import com.sagag.eshop.service.dto.offer.ShopArticleDto;
import com.sagag.services.rest.resource.DefaultResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.domain.Page;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShopArticlePageResource extends DefaultResource {

  private final Page<ShopArticleDto> shopArticles;

  public static ShopArticlePageResource of(Page<ShopArticleDto> shopArticles) {
    return new ShopArticlePageResource(shopArticles);
  }
}
