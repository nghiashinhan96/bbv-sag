package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class VehicleConstants {

  public static final float DF_BOOST = ElasticsearchConstants.DEFAULT_BOOST;

  public static final float VEH_BRAND_BOOST = 4.0f;

  public static final String HORSE_POWER_SUFFIX = "PS";

  public static final String KW_POWER_SUFFIX = "KW";

  public static final String HORSE_POWER_REGEX = "^\\d+(?i)" + HORSE_POWER_SUFFIX + "$";

  public static final String KW_POWER_REGEX = "^\\d+(?i)" + KW_POWER_SUFFIX + "$";

}
