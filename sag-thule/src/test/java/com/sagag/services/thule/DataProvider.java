package com.sagag.services.thule;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DataProvider {

  private static final String[] KEYS_OF_MAP_BY_ORDER = {"dealer", "order_list"};

  public static final String DEALER_ID = "{test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB}";

  public static final String EDITED_DEALER_ID = "test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB";

  public static final String[] SAMPLE_BUYERS_GUIDE_DATA = new String[] { DEALER_ID,
      "20201301_1|20201401_2" };

  public static final String THULE_ENDPOINT =
      "https://www.thule.com/{language}-{country}/buyers-guide?dealerId={dealerId}";

  public Map<String, String> buildMap(String... strs) {
    if (ArrayUtils.isEmpty(strs) || KEYS_OF_MAP_BY_ORDER.length != strs.length) {
      return Collections.emptyMap();
    }
    Map<String, String> map = new HashMap<>();
    IntStream.range(0, strs.length)
    .forEach(index -> map.put(KEYS_OF_MAP_BY_ORDER[index], strs[index]));
    return map;
  }

  public Map<String, String> buildSampleMap() {
    return DataProvider.buildMap(SAMPLE_BUYERS_GUIDE_DATA);
  }

  public Map<String, String> buildSampleMapMissingQuantityAndMovexId() {
    return DataProvider.buildMap(new String[] { DEALER_ID,
    "20201301_1|20201401_2|20201401|20201403_|_1" });
  }

  public Map<String, String> getDealerIdMap() {
    Map<String, String> dealerIdMap = new HashMap<>();
    dealerIdMap.put("derendinger-at", "test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB");
    dealerIdMap.put("matik-at", "test-matik-at-0C9094D8-09DB-40C5-83A8-48D22750A7DB");
    return dealerIdMap;
  }

}
