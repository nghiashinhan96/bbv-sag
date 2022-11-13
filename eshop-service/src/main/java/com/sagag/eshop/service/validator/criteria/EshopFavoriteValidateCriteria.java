package com.sagag.eshop.service.validator.criteria;

import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import lombok.Builder;
import lombok.Data;

/**
 * Criteria for validate Eshop favorite.
 */
@Data
@Builder
public class EshopFavoriteValidateCriteria {

  private final EshopFavoriteDto favoriteItem;

  private final boolean add;

  private long userId;

}
