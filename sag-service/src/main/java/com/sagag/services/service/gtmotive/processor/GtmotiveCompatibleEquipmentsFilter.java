package com.sagag.services.service.gtmotive.processor;

import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentData;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class GtmotiveCompatibleEquipmentsFilter {

  public List<GtmotiveEquipmentData> filter(List<GtmotiveEquipmentData> equipmentDatas,
      List<GtmotiveEquipmentData> functionalEquipmentDatas, List<String> vehEquipments) {
    if (CollectionUtils.isEmpty(vehEquipments)) {
      return equipmentDatas;
    }
    return CollectionUtils.emptyIfNull(equipmentDatas).stream()
        .filter(item -> !isConflictWithVehicleEquipments(functionalEquipmentDatas, vehEquipments)
            .test(item))
        .collect(Collectors.toList());
  }

  private Predicate<GtmotiveEquipmentData> isConflictWithVehicleEquipments(
      List<GtmotiveEquipmentData> functionalEquipmentDatas, List<String> vehEquipments) {
    return source -> {
      if (CollectionUtils.isEmpty(vehEquipments)) {
        return false;
      }
      List<String> conflictEquipments = collectConflictEquipments(source, functionalEquipmentDatas)
          .stream().filter(item -> !item.equalsIgnoreCase(source.safetyGetPartCode()))
          .collect(Collectors.toList());
      return conflictEquipments(conflictEquipments, vehEquipments);
    };
  }

  private boolean conflictEquipments(List<String> conflictEquipments, List<String> equipments) {
    return conflictEquipments.stream().anyMatch(conflictEquipment(equipments));
  }

  private Predicate<String> conflictEquipment(List<String> equipments) {
    return equipments::contains;
  }

  private List<String> collectConflictEquipments(GtmotiveEquipmentData source,
      List<GtmotiveEquipmentData> functionalEquipments) {
    return CollectionUtils.emptyIfNull(functionalEquipments).stream().filter(isConflictItem(source))
        .map(GtmotiveEquipmentData::safetyGetPartCode).filter(StringUtils::isNoneEmpty).distinct()
        .collect(Collectors.toList());
  }

  private Predicate<GtmotiveEquipmentData> isConflictItem(GtmotiveEquipmentData target) {
    return source -> {
      List<String> incompatibilityGroups = source.getIncompatibilityGroupList();
      List<String> functionalIncompatibilityGroups = target.getIncompatibilityGroupList();
      if (CollectionUtils.isEmpty(incompatibilityGroups)
          || CollectionUtils.isEmpty(functionalIncompatibilityGroups)) {
        return false;
      }

      return isConflicts(functionalIncompatibilityGroups).test(incompatibilityGroups);
    };
  }

  private Predicate<List<String>> isConflicts(List<String> functionalIncompatibilityGroups) {
    return incompatibilityGroups -> incompatibilityGroups.stream()
        .anyMatch(isConflict(functionalIncompatibilityGroups));
  }

  private Predicate<String> isConflict(List<String> functionalIncompatibilityGroups) {
    return functionalIncompatibilityGroups::contains;
  }
}
