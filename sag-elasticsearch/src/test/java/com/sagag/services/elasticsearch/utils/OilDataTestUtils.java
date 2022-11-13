package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.utils.PageUtils;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;

@UtilityClass
public final class OilDataTestUtils {

  public static final List<String> BOTTLE_SIZE_0DOT125 = Arrays.asList("0.125");
  public static final List<String> BOTTLE_SIZE_0DOT3 = Arrays.asList("0.30");
  public static final List<String> BOTTLE_SIZE_60 = Arrays.asList("60");
  public static final List<String> BOTTLE_SIZE_20 = Arrays.asList("20");
  public static final List<String> BOTTLE_SIZE_2 = Arrays.asList("2");
  public static final List<String> BOTTLE_SIZES_1_60 = Arrays.asList("1", "60");

  public static final List<String> VISCOSITY_10W40 = Arrays.asList("10W40");
  public static final List<String> VISCOSITY_10W = Arrays.asList("10W");

  public static final List<String> VEHICLE_MOTORAD = Arrays.asList("Motorrad");
  public static final List<String> VEHILCE_LKW = Arrays.asList("LKW");

  public static final List<String> SPEC_ISOLEGD = Arrays.asList("ISO-L-EGD");
  public static final List<String> SPEC_SF = Arrays.asList("SF");
  public static final List<String> SPECS_A3_B4 = Arrays.asList("A3 B4");

  public static final List<String> APPROVED_35VQ25 = Arrays.asList("35VQ25");
  public static final List<String> APPROVED_505 = Arrays.asList("505");

  public static final List<String> SUPPLIERS_ENI_MOBIL = Arrays.asList("ENI", "MOBIL");

  public static final List<String> AGGREGATE_HYDRAULIK = Arrays.asList("Hydraulik");

  public static final int TOTAL_ELEMENTS_OF_SEARCHING = 100;

  public static final Pageable PAGEABLE_WITH_TOTAL_ELEMENTS_OF_SEARCHING =
      PageUtils.defaultPageable(TOTAL_ELEMENTS_OF_SEARCHING);


}
