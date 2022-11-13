package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.utils.PageUtils;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;

@UtilityClass
public final class BulbDataTestUtils {

  public static final List<String> SUPPLIER_HELLA = Arrays.asList("HELLA");
  public static final List<String> SUPPLIERS_HELLA_OSRAM = Arrays.asList("HELLA", "OSRAM");
  public static final List<String> VOLTAGE_24 = Arrays.asList("24");
  public static final List<String> WATT_10 = Arrays.asList("10");
  public static final List<String> CODE_K100W = Arrays.asList("K (10W)");
  public static final List<String> CODE_F2 = Arrays.asList("F2");
  public static final List<String> CODE_H2 = Arrays.asList("H2");
  public static final List<String> CODES_H10W_K10W_R10W = Arrays.asList("H10W", "K (10W)", "R10W");

  public static final int TOTAL_ELEMENTS_OF_SEARCHING = 100;

  public static final Pageable PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING =
      PageUtils.defaultPageable(TOTAL_ELEMENTS_OF_SEARCHING);


}
