package com.sagag.services.common.contants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Tyre Constants for searching tyre articles.
 *
 * <p>The constants class to support searching tyre articles</p>
 *
 */
public class TyreConstants {

  public static final String PRIORITY_PRODUCT_GROUP_P = "P";

  public static final String PRIORITY_PRODUCT_GROUP_R = "R";

  public static final String PRIORITY_PRODUCT_GROUP_I = "I";

  public static final int VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE = 300;

  public static final int WIDTH_CID = 9075;

  public static final int HEIGHT_CID = 9076;

  public static final int RADIUS_CID = 9077;

  public static final int SPEED_INDEX_CID = 9078;

  public static final int TYRE_SEGMENT_CID = 1488;

  public static final int LOAD_INDEX_CID = 1431;

  public static final String RUNFLAT_CVP = "Run Flat";

  public static final String SPIKE_CVP = "Spike";

  public static final String WIDTH_MAP_NAME = "widths";

  public static final String HEIGHT_MAP_NAME = "heights";

  public static final String RADIUS_MAP_NAME = "radiuses";

  public static final String SPEED_INDEX_MAP_NAME = "speed_indexes";

  public static final String SEASON_MAP_NAME = "seasons";

  public static final String TYRE_SEGMENT_MAP_NAME = "tyre_segments";

  public static final String SUPPLIER_MAP_NAME = "suppliers";

  public static final String FZ_CATEGORY_MAP_NAME = "fz_categories";

  public static final String LOAD_INDEX_MAP_NAME = "load_indexs";

  public static final int MENGE_OF_TYRES_CAR_VEHICLE = 4;

  public static final String MOTORBIKE_GEN_ART_ID_1 = "111800";

  public static final String MOTORBIKE_GEN_ART_ID_2 = "11810";

  public static final String MOTOR_CATEGORY_MAP_NAME = "motor_categories";

  @Getter
  @AllArgsConstructor
  public enum Season {
    SUMMER(Arrays.asList("11697", "1169710", "116971")),
    WINTER(Arrays.asList("11698", "1169810", "116981")),
    ALL_YEAR(Arrays.asList("11699", "1169910", "116991"));

    private List<String> genArtIds;
  }

  @Getter
  @AllArgsConstructor
  public enum FzCategory {
    PKW(Arrays.asList("11697", "11698", "11699")),
    FOUR_X_FOUR(Arrays.asList("1169710", "1169810", "1169910")),
    LNFZ(Arrays.asList("116971", "116981", "116991"));

    private List<String> genArtIds;
  }

  @Getter
  @AllArgsConstructor
  public enum MotorbikeCategory {
    STRASSENREIFEN(Arrays.asList("1170000")),
    ROLLER(Arrays.asList("1170001")),
    OFFROAD(Arrays.asList("1170002")),
    RENNREIFEN(Arrays.asList("1170003"));

    private List<String> genArtIds;
  }

}
