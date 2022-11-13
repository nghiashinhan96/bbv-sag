package com.sagag.services.service.gtmotive.processor;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveReferenceSearchByPartCodesCriteria;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentData;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionBaseItem;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.service.request.gtmotive.GtmotiveOperationItem;
import com.sagag.services.service.response.gtmotive.GtmotiveEquipmentOptionsDto;
import com.sagag.services.service.response.gtmotive.GtmotiveEquipmentOptionsFamily;
import com.sagag.services.service.response.gtmotive.GtmotiveEquipmentOptionsSubFamily;
import com.sagag.services.service.response.gtmotive.GtmotiveReferenceSearchResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathExpressionException;

@Component
public class GtmotiveReferenceSearchProcessor {

  private static final int GREEN_PART_ERROR = 32;

  @Autowired
  private GtmotiveService gtmotiveService;

  @Autowired
  private GtmotiveCompatibleEquipmentsFilter gtmotiveCompatibleEquipmentsFilter;

  public GtmotiveReferenceSearchResponse process(
      GtmotiveReferenceSearchByPartCodesCriteria criteria)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {

    String partCode = Optional.ofNullable(criteria.getGtmotivePartsThreeSearchRequest())
        .map(GtmotivePartsThreeSearchRequest::getPartCode).orElse(StringUtils.EMPTY);
    GtmotiveReferenceSearchResponse response = new GtmotiveReferenceSearchResponse();
    if (!criteria.isVinMode() && !criteria.isMaintenance()) {
      response.setCupi(partCode);
      return response;
    }

    GtmotivePartsThreeSearchRequest gtmotivePartsThreeSearchRequest =
        criteria.getGtmotivePartsThreeSearchRequest();
    GtmotivePartsThreeResponse gtmotivePartsThreeResponse =
        gtmotiveService.searchReferencesByPartCode(gtmotivePartsThreeSearchRequest);
    if (Objects.isNull(gtmotivePartsThreeResponse)) {
      return null;
    }

    if (gtmotivePartsThreeResponse.getErrorCode() == GREEN_PART_ERROR) {
      List<GtmotiveEquipmentOptionsFamily> families =
          getFinalEquipmentOptionFamilies(partCode, gtmotivePartsThreeSearchRequest);
      response.setEquipmentOptionFamilies(families);
    }

    response.setPartCode(partCode);

    List<GtmotiveOperationItem> operations = CollectionUtils
        .emptyIfNull(gtmotivePartsThreeResponse.getReferences()).stream()
        .map(GtmotiveOperationItem::fromGtmotivePartsThreeReference).collect(Collectors.toList());

    response.setOperations(operations);
    return response;
  }

  private List<GtmotiveEquipmentOptionsFamily> getFinalEquipmentOptionFamilies(String partCode,
      GtmotivePartsThreeSearchRequest gtmotivePartsThreeSearchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {

    GtmotiveEquipmentOptionsSearchRequest gtEquipmentOptionsSearchRequest =
        GtmotiveEquipmentOptionsSearchRequest.builder()
            .umc(gtmotivePartsThreeSearchRequest.getUmc()).partCodes(Arrays.asList(partCode))
            .build();
    GtmotiveEquipmentOptionsResponse gtEquipmentOptionsResponse =
        gtmotiveService.searchEquipmentOptions(gtEquipmentOptionsSearchRequest);


    GtmotiveEquipmentOptionsSearchRequest gtFunctionalGroupEquipmentOptionsSearchRequest =
        GtmotiveEquipmentOptionsSearchRequest.builder()
            .umc(gtmotivePartsThreeSearchRequest.getUmc())
            .functionalGroup(gtmotivePartsThreeSearchRequest.getFunctionalGroup()).build();
    GtmotiveEquipmentOptionsResponse gtFunctionalEquipmentOptionsResponse =
        gtmotiveService.searchEquipmentOptions(gtFunctionalGroupEquipmentOptionsSearchRequest);

    List<GtmotiveEquipmentData> compatibleEquipments =
        gtmotiveCompatibleEquipmentsFilter.filter(gtEquipmentOptionsResponse.getEquipmentDatas(),
            gtFunctionalEquipmentOptionsResponse.getEquipmentDatas(),
            gtmotivePartsThreeSearchRequest.getEquipments());
    return groupByFamily(compatibleEquipments);
  }

  private List<GtmotiveEquipmentOptionsFamily> groupByFamily(
      List<GtmotiveEquipmentData> equipmentDatas) {
    List<GtmotiveEquipmentData> validEquipments = CollectionUtils.emptyIfNull(equipmentDatas)
        .stream().filter(GtmotiveEquipmentData::isValidItem).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(validEquipments)) {
      return Collections.emptyList();
    }

    Map<GtmotiveEquipmentOptionsFamily, Map<GtmotiveEquipmentOptionsSubFamily, List<GtmotiveEquipmentOptionsDto>>> map =
        validEquipments.stream().collect(
            Collectors.groupingBy(collectFamily(), sortedFamilyTreeMap(), Collectors.groupingBy(
                collectSubFamily(), sortedSubFamilyTreeMap(), collectEquipmentOptions())));
    return convertToFamilyList(map);
  }

  private List<GtmotiveEquipmentOptionsFamily> convertToFamilyList(
      Map<GtmotiveEquipmentOptionsFamily, Map<GtmotiveEquipmentOptionsSubFamily, List<GtmotiveEquipmentOptionsDto>>> map) {
    return map.entrySet().stream().map(item -> {
      GtmotiveEquipmentOptionsFamily family = item.getKey();
      Map<GtmotiveEquipmentOptionsSubFamily, List<GtmotiveEquipmentOptionsDto>> subFamilyMap =
          item.getValue();
      family.setSubFamilies(convertToSubfamilyList(subFamilyMap));
      return family;
    }).collect(Collectors.toList());
  }


  private List<GtmotiveEquipmentOptionsSubFamily> convertToSubfamilyList(
      Map<GtmotiveEquipmentOptionsSubFamily, List<GtmotiveEquipmentOptionsDto>> map) {
    return map.entrySet().stream().map(item -> {
      GtmotiveEquipmentOptionsSubFamily subFamily = item.getKey();
      List<GtmotiveEquipmentOptionsDto> equipments = item.getValue();
      subFamily.setEquipmentOptions(equipments);
      return subFamily;
    }).collect(Collectors.toList());
  }

  private Function<GtmotiveEquipmentData, GtmotiveEquipmentOptionsFamily> collectFamily() {
    return eq -> {
      GtmotiveEquipmentOptionBaseItem family = eq.getFamily();
      return GtmotiveEquipmentOptionsFamily.builder().code(family.getCode())
          .description(family.getDescription()).build();
    };
  }

  private Function<GtmotiveEquipmentData, GtmotiveEquipmentOptionsSubFamily> collectSubFamily() {
    return eq -> {
      GtmotiveEquipmentOptionBaseItem subFamily = eq.getSubfamily();
      return GtmotiveEquipmentOptionsSubFamily.builder().code(subFamily.getCode())
          .description(subFamily.getDescription()).build();
    };
  }

  private Collector<GtmotiveEquipmentData, ?, List<GtmotiveEquipmentOptionsDto>> collectEquipmentOptions() {
    return Collectors.mapping(toEquipmentOptionDto(), Collectors.toList());
  }

  private Function<GtmotiveEquipmentData, GtmotiveEquipmentOptionsDto> toEquipmentOptionDto() {
    return eq -> SagBeanUtils.map(eq, GtmotiveEquipmentOptionsDto.class);
  }

  private Supplier<Map<GtmotiveEquipmentOptionsSubFamily, List<GtmotiveEquipmentOptionsDto>>> sortedSubFamilyTreeMap() {
    return () -> new TreeMap<>((a, b) -> {
      if (a.getCode().equals(b.getCode())) {
        return 0;
      }
      return 1;
    });
  }

  private Supplier<Map<GtmotiveEquipmentOptionsFamily, Map<GtmotiveEquipmentOptionsSubFamily, List<GtmotiveEquipmentOptionsDto>>>> sortedFamilyTreeMap() {
    return () -> new TreeMap<>((a, b) -> {
      if (a.getCode().equals(b.getCode())) {
        return 0;
      }
      return 1;
    });
  }
}
