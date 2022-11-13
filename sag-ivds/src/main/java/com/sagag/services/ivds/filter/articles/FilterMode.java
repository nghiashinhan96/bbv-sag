package com.sagag.services.ivds.filter.articles;

import com.sagag.eshop.repo.enums.ArticleShopType;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration of articles filter mode.
 */
@Getter
@AllArgsConstructor
public enum FilterMode {

  FREE_TEXT(Optional.empty()), // free text filtering,
  ARTICLE_NUMBER(Optional.empty()), // filter by article number,
  ONLY_ARTICLE_NUMBER_AND_SUPPLIER(Optional.empty()), // filter only by article number and supplier,
  ID_SAGSYS(Optional.empty()), // filter by id sagsys,
  TYRES_SEARCH(Optional.of(ArticleShopType.TYRES)), // filter tyre,
  MOTOR_TYRES_SEARCH(Optional.of(ArticleShopType.TYRES)), // search moto tyre,
  BULB_SEARCH(Optional.of(ArticleShopType.BATTERIES)), // filter battery,
  BATTERY_SEARCH(Optional.of(ArticleShopType.BULBS)), // filter bulb,
  OIL_SEARCH(Optional.of(ArticleShopType.OILS)), // filter oil,
  EAN_CODE(Optional.empty()),
  BAR_CODE(Optional.empty()),
  MATCH_CODE(Optional.of(ArticleShopType.TYRES)),
  WSP_SEARCH(Optional.of(ArticleShopType.WSP)),
  ACCESSORY_LIST(Optional.empty()), // filter accessory list
  PART_LIST(Optional.empty()),
  CROSS_REFERENCE(Optional.empty()),  // Filter cross-reference
  EXTERNAL_PARTS(Optional.empty());

  private Optional<ArticleShopType> shopType;

  public boolean isNormalizeKeyword() {
    return ARTICLE_NUMBER != this
        && TYRES_SEARCH != this
        && MOTOR_TYRES_SEARCH != this
        && BATTERY_SEARCH != this
        && BULB_SEARCH != this
        && OIL_SEARCH != this
        && MATCH_CODE != this
        && WSP_SEARCH != this;
  }

  public boolean isTyres() {
    return TYRES_SEARCH == this || MOTOR_TYRES_SEARCH == this || MATCH_CODE == this;
  }

  public boolean isWSPSearch() {
    return WSP_SEARCH == this;
  }
  
  public boolean isPartList() {
    return PART_LIST == this;
  }

  public boolean isAccessorySearch() {
    return ACCESSORY_LIST == this;
  }

  public boolean isStoredInCachedSpecialShop() {
    return isStoredInCached(this);
  }

  public boolean isStoredInCachedPerfectMatch() {
    return FilterMode.ARTICLE_NUMBER == this || FilterMode.FREE_TEXT == this;
  }

  public boolean isNonStoredInCached() {
    return !this.isStoredInCachedSpecialShop() && !isStoredInCachedPerfectMatch();
  }

  public static boolean isNonStoredInCacheSpecialShop(FilterMode filterMode) {
    return FilterMode.WSP_SEARCH == filterMode
        || FilterMode.ACCESSORY_LIST == filterMode
        || FilterMode.PART_LIST == filterMode;
  }

  public boolean isCrossReferenceSearch() {
    return Objects.equals(this, CROSS_REFERENCE);
  }

  public boolean isExternalParts() {
    return Objects.equals(this, FilterMode.FREE_TEXT) || Objects.equals(this, FilterMode.ARTICLE_NUMBER);
  }

  private static boolean isStoredInCached(FilterMode filterMode) {
    return FilterMode.TYRES_SEARCH == filterMode
        || FilterMode.MOTOR_TYRES_SEARCH == filterMode
        || FilterMode.BULB_SEARCH == filterMode
        || FilterMode.BATTERY_SEARCH == filterMode
        || FilterMode.OIL_SEARCH == filterMode
        || FilterMode.MATCH_CODE == filterMode;
  }

}
