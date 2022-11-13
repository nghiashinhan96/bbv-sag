package com.sagag.services.elasticsearch.common;

import lombok.experimental.UtilityClass;

/**
 * Bulb Constants for searching bulb articles.
 *
 * <p>The constants class to support searching bulb articles</p>
 *
 */
@UtilityClass
public final class BulbConstants {

  public static final int VOLTAGE_CID = 6;

  public static final int WATT_CID = 53;

  public static final int CODE_CID = 437;

  public static final int ARTICLES_MAX_PAGE_SIZE = 2000;

  public static final int VIEW_ARTICLES_MAX_PAGE_SIZE = 300;

  public static final String VOLTAGE_MAP_NAME = "voltages";

  public static final String WATT_MAP_NAME = "watts";

  public static final String CODE_MAP_NAME = "codes";

  public static final String SUPPLIER_MAP_NAME = "suppliers";

}
