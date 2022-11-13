package com.sagag.services.elasticsearch.common;

import lombok.experimental.UtilityClass;

/**
 * Battery Constants for searching battery articles.
 *
 * <p>The constants class to support searching battery articles</p>
 *
 */
@UtilityClass
public final class BatteryConstants {

  public static final int VOLTAGE_CID = 6;

  public static final int AMPERE_HOUR_CID = 68;

  public static final int LENGTH_CID = 203;

  public static final int WIDTH_CID = 206;

  public static final int HEIGHT_CID = 209;

  public static final int INTERCONNECTION_CID = 77;

  public static final int POLE_CID = 899;

  public static final int WITHOUT_START_STOP_CID = 100001;

  public static final int WITH_START_STOP_CID = 100002;

  public static final int VIEW_ARTICLES_MAX_PAGE_SIZE = 300;

  public static final String VOLTAGE_MAP_NAME = "voltages";

  public static final String AMPERE_HOUR_MAP_NAME = "ampere_hours";

  public static final String LENGTH_MAP_NAME = "lengths";

  public static final String WIDTH_MAP_NAME = "widths";

  public static final String HEIGHT_MAP_NAME = "heights";

  public static final String INTERCONNECTION_MAP_NAME = "interconnections";

  public static final String POLE_MAP_NAME = "poles";

}
