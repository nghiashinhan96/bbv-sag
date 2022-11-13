package com.sagag.eshop.repo.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Enumerations of Eshop Favorite Type Source.
 */
public enum EshopFavoriteType {
  LEAF_NODE, ARTICLE, VEHICLE;

  public EshopFavoriteType getFavoriteType(String type) {
    return EshopFavoriteType.valueOf(type);
  }

  public static EshopFavoriteType valueOfSafely(String type) {
    if (StringUtils.isBlank(type)) {
      return null;
    }
    return EshopFavoriteType.valueOf(type);
  }
}
