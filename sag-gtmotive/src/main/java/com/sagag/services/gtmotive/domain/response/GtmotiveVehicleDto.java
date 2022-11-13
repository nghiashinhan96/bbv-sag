package com.sagag.services.gtmotive.domain.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVehicleInfoResponse;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({ "vin", "umc", "equipmentItems", "equipmentRanks", "estimateId" })
public class GtmotiveVehicleDto implements Serializable {

  private static final long serialVersionUID = -8800003970105188094L;

  private String estimateId;

  private String vin;

  private String umc;

  private List<String> equipmentItems;

  private List<EquipmentRank> equipmentRanks;

  private String makeCode;

  private List<String> modelCodes;

  private List<String> engineCodes;

  private List<String> transmisionCodes;

  public static Function<GtmotiveVehicleInfoResponse, Optional<GtmotiveVehicleDto>> converter() {
    return response -> {
      if (response == null) {
        return Optional.empty();
      }
      final GtmotiveVehicleInfo vehicleInfo = response.getVehicleInfo();
      final List<EquipmentItem> equipmentItemList = vehicleInfo.getEquipments();
      final List<String> equipmentItems = equipmentItemList.stream()
        .map(EquipmentItem::getValue).collect(Collectors.toList());

      String langIsoCode = response.getUserInfo().getCultureIsoCode();
      final Map<VehicleItemEnum, List<String>> equipmentCodes = GtmotiveUtils.extractGtmotiveCodes(
        equipmentItemList, langIsoCode );

      return Optional.of(GtmotiveVehicleDto.builder()
        .estimateId(defaultEstimateId(response.getEstimateId()))
        .vin(vehicleInfo.getVin().getValue())
        .umc(vehicleInfo.getUmc().getValue())
        .equipmentItems(equipmentItems)
        .equipmentRanks(getEquipmentRanks(response))
        .modelCodes(equipmentCodes.getOrDefault(VehicleItemEnum.MODEL, Collections.emptyList()))
        .engineCodes(equipmentCodes.getOrDefault(VehicleItemEnum.ENGINE, Collections.emptyList()))
        .transmisionCodes(equipmentCodes.getOrDefault(VehicleItemEnum.TRANSMISSION,
          Collections.emptyList()))
        .makeCode(vehicleInfo.getMakeCode().getValue())
        .build());
    };
  }

  private static List<EquipmentRank> getEquipmentRanks(GtmotiveVehicleInfoResponse response) {
    return response.getVehicleInfo().getRanks().stream()
      .map(rank -> EquipmentRank.builder()
        .value(rank.getValue())
        .family(rank.getFamily())
        .subFamily(rank.getSubFamily())
        .build())
      .collect(Collectors.toList());
  }

  private static String defaultEstimateId(GtmotiveEstimate estimate) {
    if (estimate == null) {
      return StringUtils.EMPTY;
    }
    return StringUtils.defaultString(estimate.getId());
  }
}
