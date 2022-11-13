package com.sagag.services.hazelcast.app;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum HazelcastMaps {

  // User token maps
  ACCESS_TOKEN_STORE(HazelcastConstants.TTL_8_HOURS),// @formatter:off
  AUTHENTICATION_TO_ACCESS_TOKEN_STORE(HazelcastConstants.TTL_8_HOURS),
  USER_NAME_TO_ACCESS_TOKEN_STORE(HazelcastConstants.TTL_8_HOURS),
  CLIENT_ID_TO_ACCESS_TOKEN_STORE(HazelcastConstants.TTL_8_HOURS),
  REFRESH_TOKEN_STORE(HazelcastConstants.TTL_8_HOURS),
  ACCESS_TOKEN_TO_REFRESH_TOKEN_STORE(HazelcastConstants.TTL_8_HOURS),
  AUTHENTICATION_STORE(HazelcastConstants.TTL_8_HOURS),
  REFRESH_TOKEN_AUTHENTICATION_STORE(HazelcastConstants.TTL_8_HOURS),
  REFRESH_TOKEN_TO_ACCESS_TOKEN_STORE(HazelcastConstants.TTL_8_HOURS),
  EXPIRY_MAP(HazelcastConstants.TTL_8_HOURS),

  // Eshop Client details
  ESHOP_CLIENT_DETAILS(HazelcastConstants.TTL_8_HOURS),

  // Shopping cart maps
  SHOPPING_CART_MAP(HazelcastConstants.TTL_FOREVER),
  TOTAL_CART_MAP(HazelcastConstants.TTL_FOREVER),

  // Categories and other static data maps
  CATEGORY_MAP(HazelcastConstants.TTL_FOREVER, true),
  SUPPLIER_MAP(HazelcastConstants.TTL_FOREVER),
  MAKE_MAP(HazelcastConstants.TTL_FOREVER),
  MODEL_MAP(HazelcastConstants.TTL_FOREVER, true),
  GEN_ART_MAP(HazelcastConstants.TTL_FOREVER, true),
  CRITERIA_MAP(HazelcastConstants.TTL_FOREVER, true),
  FORMAT_GA_MAP(HazelcastConstants.TTL_FOREVER, true),
  EXTERNAL_VENDOR_MAP(HazelcastConstants.TTL_FOREVER),
  DELIVERY_PROFILE_MAP(HazelcastConstants.TTL_FOREVER),
  GLOBAL_SETTING_MAP(HazelcastConstants.TTL_FOREVER),
  BRAND_PRIORITY_MAP(HazelcastConstants.TTL_FOREVER),
  SPRING_SCHEDULER_LOCK_MAP(HazelcastConstants.TTL_FOREVER),

  // VAT rate data maps
  VAT_RATE_MAP(HazelcastConstants.TTL_FOREVER),

  // Application context maps
  CONTEXT_MAP(HazelcastConstants.TTL_4_HOURS),
  HAYNESPRO_MAP(HazelcastConstants.TTL_1_HOUR),

  NEXT_WORKING_DATE_MAP(HazelcastConstants.TTL_8_HOURS),
  COUPON_USE_LOG(HazelcastConstants.TTL_8_HOURS),
  DMS_EXPORT(HazelcastConstants.TTL_8_HOURS),
  ACTIVE_DMS_USER(HazelcastConstants.TTL_8_HOURS),
  VIN_SEARCH_COUNT(HazelcastConstants.TTL_8_HOURS),
  ESHOP_CONTEXT_CACHE(HazelcastConstants.TTL_8_HOURS),
  SEARCH_RESULT_MAP(HazelcastConstants.TTL_1_HOUR),

  // User session maps
  SESSION_USER_MAP(HazelcastConstants.TTL_8_HOURS),
  DVSE_USER_MAP(HazelcastConstants.TTL_1_HOUR),
  SESSION_CUSTOMER_MAP(HazelcastConstants.TTL_1_HOUR),
  SESSION_BRANCH_MAP(HazelcastConstants.TTL_1_HOUR),
  SESSION_COURIER_MAP(HazelcastConstants.TTL_1_HOUR),
  // @formatter:on

  // Autonet session maps
  AUTONET_SESSION(HazelcastConstants.TTL_8_HOURS),

  // OATS Vehicle maps
  OATES_VEHICLE_MAP(HazelcastConstants.TTL_1_HOUR),
  ADDITIONAL_RECOMMENDATIONS_MAP(HazelcastConstants.TTL_FOREVER, true),

  // Tour Time maps
  TOUR_TIME_MAP(HazelcastConstants.TTL_8_HOURS);

  @Getter
  private final int ttl;

  @Getter
  private final boolean multilingual;

  /**
   * Constructors with disable multilingual mode.
   */
  HazelcastMaps(int ttl) {
    this.ttl = ttl;
    this.multilingual = false;
  }

  /**
   * Checks if this is the shopping cart cache map.
   *
   * @return <code>true</code> if this is the shopping cart cache map, <code>false</code> otherwise.
   */
  public boolean isShoppingCartMap() {
    return this == SHOPPING_CART_MAP;
  }

  public boolean isCategoryMap() {
    return CATEGORY_MAP == this;
  }

  public boolean isModelMap() {
    return MODEL_MAP == this;
  }

  public boolean isGenartMap() {
    return GEN_ART_MAP == this;
  }

  public boolean isCriteriaMap() {
    return CRITERIA_MAP == this;
  }

  public boolean isFormatGaMap() {
    return FORMAT_GA_MAP == this;
  }

  public boolean isSupplierMap() {
    return SUPPLIER_MAP == this;
  }

  public boolean isMakeMap() {
    return MAKE_MAP == this;
  }

  public boolean isVatRateMap() {
    return VAT_RATE_MAP == this;
  }

  public boolean isOnNearCacheMode() {
  return isCategoryMap()
        || isModelMap()
        || isGenartMap()
        || isCriteriaMap()
        || isFormatGaMap()
        || isSupplierMap()
        || isMakeMap()
        || isVatRateMap();
  }
}
