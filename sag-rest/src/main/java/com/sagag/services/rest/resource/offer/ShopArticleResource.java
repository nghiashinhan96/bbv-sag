package com.sagag.services.rest.resource.offer;

import com.sagag.eshop.service.dto.offer.ShopArticleDto;
import com.sagag.services.rest.resource.DefaultResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShopArticleResource extends DefaultResource {

  private final ShopArticleDto shopArticle;

  public static ShopArticleResource of(ShopArticleDto shopArticle) {
    return new ShopArticleResource(shopArticle);
  }

}
