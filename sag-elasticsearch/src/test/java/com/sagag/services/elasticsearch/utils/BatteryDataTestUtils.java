package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.PageUtils;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public final class BatteryDataTestUtils {

  public static final List<String> DEFAULT_VOLTAGE = Arrays.asList("12");

  public static final List<String> AMPERE_HOUR_1 = Arrays.asList("1");

  public static final List<String> AMPERE_HOUR_13DOT7 = Arrays.asList("13.70");

  public static final List<String> AMPERE_HOUR_120 = Arrays.asList("120");

  public static final List<String> AMPERE_HOUR_120_155_180 = Arrays.asList("120", "155", "180");

  public static final List<String> INTERCONNECTION_1 = Arrays.asList("1");

  public static final List<String> INTERCONNECTION_4 = Arrays.asList("4");

  public static final List<String> WIDTH_100 = Arrays.asList("100");

  public static final List<String> WIDTH_175 = Arrays.asList("175");

  public static final List<String> WIDTH_90 = Arrays.asList("90");

  public static final List<String> WIDTH_189 = Arrays.asList("189");

  public static final List<String> POLE_1ANDS = Arrays.asList("1 & S");

  public static final List<String> POLE_3 = Arrays.asList("3");

  public static final List<String> POLE_1 = Arrays.asList("1");

  public static final List<String> POLE_Y8 = Arrays.asList("Y8");

  public static final List<String> HEIGHT_100 = Arrays.asList("100");

  public static final List<String> HEIGHT_215 = Arrays.asList("215");

  public static final List<String> HEIGHT_130 = Arrays.asList("130");

  public static final List<String> HEIGHT_223 = Arrays.asList("223");

  public static final List<String> LENGTH_328 = Arrays.asList("328");

  public static final List<String> LENGTH_100 = Arrays.asList("100");

  public static final List<String> LENGTH_175 = Arrays.asList("175");

  public static final List<String> LENGTH_513 = Arrays.asList("513");

  public static final List<String> SUPPLIER_BOSCH = Arrays.asList("BOSCH");

  public static final List<String> SUPPLIER_EXIDE_BATTERIES = Arrays.asList("EXIDE BATTERIES");

  public static final List<String> SUPPLIER_BOSCH_VARTA = Arrays.asList("BOSCH", "VARTA");

  public static final int TOTAL_ELEMENTS_OF_SEARCHING = 165;

  public static final PageRequest PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING =
      PageUtils.defaultPageable(TOTAL_ELEMENTS_OF_SEARCHING);

  public static final PageRequest DF_PAGE_REQUEST =
      PageUtils.defaultPageable(SagConstants.MAX_PAGE_SIZE);

}
