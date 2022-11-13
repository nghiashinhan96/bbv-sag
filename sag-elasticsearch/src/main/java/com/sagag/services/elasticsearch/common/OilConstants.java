package com.sagag.services.elasticsearch.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class OilConstants {

  public static final int ARTICLES_MAX_PAGE_SIZE = 2000;

  public static final int VIEW_ARTICLES_MAX_PAGE_SIZE = 300;

  /* Fahrzeug-Art */
  public static final int VEHICLE_CID = 100003;

  /* Aggregat */
  public static final int AGGREGATE_CID = 100004;

  /* Viskosität */
  public static final int VISCOSITY_CID = 1054;

  /* Gebindegrösse */
  public static final int BOTTLE_SIZE_CID = 423;

  /* Freigabe */
  public static final int APPROVED_CID = 1161;

  /* Spezifikation */
  public static final int SPECIFICATION_CID = 1073;

  public static final String VEHICLE_MAP_NAME = "vehicles";

  public static final String AGGREGATE_MAP_NAME = "aggregates";

  public static final String VISCOSITY_MAP_NAME = "viscosities";

  public static final String BOTTLE_SIZE_MAP_NAME = "bottle_sizes";

  public static final String APPROVED_MAP_NAME = "approved_list";

  public static final String SPECIFICATION_MAP_NAME = "specifications";

  public static final String BRAND_MAP_NAME = "brands";

}
