package com.sagag.services.gtmotive.utils;

import com.google.common.collect.ImmutableList;
import com.sagag.services.common.utils.SagStringUtils;
import com.sagag.services.gtmotive.domain.response.EquipmentItem;
import com.sagag.services.gtmotive.domain.response.SubFamilyDescMapping;
import com.sagag.services.gtmotive.domain.response.VehicleItemEnum;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public final class GtmotiveUtils {

  private static final String GT_SUB_FAMILY_TRA = "TRA";

  private static final String RESULT_TERM = "<GTIServiceResult>";

  private static final String RESULT_CLOSED_TERM = "</GTIServiceResult>";

  public static final String GT_VIN_QUERY = "vinquery";

  public static final String GT_FAMILY_MOT = "MOT";

  public static final String GT_FAMILY_CAM = "CAM";

  public static final ImmutableList<String> GT_SUB_FAMILY_ALLOWED =
      ImmutableList.of("BIF", "CAR", "DSL", "GNC", "GPL", "IMO", "IMU", "INY", "TDS");

  private static final String[] SEARCH_CHARS = { "o", "O" };

  private static final String[] REPLACEMENT_CHARS = { "0", "0" };

  public static final String AUTOMATIC = "automatic";

  public static String generateEstimateId(final String customerNr) {
    if (StringUtils.isBlank(customerNr)) {
      return String.valueOf(System.currentTimeMillis());
    }
    return StringUtils.join(customerNr, System.currentTimeMillis());
  }

  public static String extractServiceResult(final String unescapseXmlData) {
    return StringUtils.substring(unescapseXmlData,
      StringUtils.indexOf(unescapseXmlData, RESULT_TERM) + RESULT_TERM.length(),
      StringUtils.indexOf(unescapseXmlData, RESULT_CLOSED_TERM));
  }

  public static Map<VehicleItemEnum, List<String>> extractGtmotiveCodes(
      final List<EquipmentItem> equipments, String langIsoCode) {
    if (CollectionUtils.isEmpty(equipments)) {
      return Collections.emptyMap();
    }

    return equipments.stream()
      .filter(filterEquipmentConditions(langIsoCode))
      .collect(Collectors.groupingBy(
        equipItem -> VehicleItemEnum.valueOfFamilyCode(equipItem.getFamily()), HashMap::new,
        Collectors.mapping(equipItem -> SagStringUtils.handleElasticBlank(equipItem.getValue()),
          Collectors.toList())));
  }

  public static BiFunction<List<String>, Set<String>, List<String>> nonMatchedGaIdsConverter() {
    return (gtmotiveSelectedGaids, allGaids) -> {
      if (CollectionUtils.isEmpty(gtmotiveSelectedGaids)
          || CollectionUtils.isEmpty(allGaids)) {
          return Collections.emptyList();
        }

        gtmotiveSelectedGaids.removeIf(allGaids::contains);
        return Collections.unmodifiableList(gtmotiveSelectedGaids);
    };
  }

  public Predicate<String> startsWithIgnoreCaseSpecialEquipmentPrefixPredicate() {
    return item -> StringUtils.startsWithIgnoreCase(item,
      GtmotiveConstant.SPECIAL_EQUIPMENT_PREFIX);
  }

  public Function<String, String> stripStartPartNumberConverter() {
    return partNr -> StringUtils.stripStart(partNr,
        GtmotiveConstant.GT_MOTIVE_STRIP_PART_NUMBER_CHARACTER);
  }

  public String modifyVinCode(String vin) {
    return StringUtils.replaceEach(vin, SEARCH_CHARS, REPLACEMENT_CHARS);
  }

  private static Predicate<EquipmentItem> filterEquipmentConditions(String langIsoCode) {
    return equipmentItem -> {
      // PBI-6225
      if (VehicleItemEnum.TRANSMISSION == VehicleItemEnum
          .valueOfFamilyCode(equipmentItem.getFamily())) {
        return is4x4TranmissionEquipment(equipmentItem, langIsoCode);
      }
      return VehicleItemEnum.contains(equipmentItem.getFamily());
    };
  }

  private static boolean is4x4TranmissionEquipment(EquipmentItem equipmentItem,
      String langIsoCode) {
    return GT_SUB_FAMILY_TRA.equals(equipmentItem.getSubFamily()) && StringUtils.equalsIgnoreCase(
        SubFamilyDescMapping.getDescFromCode(langIsoCode), equipmentItem.getSubFamilyDescription());
  }
}
