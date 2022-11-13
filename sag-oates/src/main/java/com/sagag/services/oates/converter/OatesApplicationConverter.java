package com.sagag.services.oates.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.oates.config.OatesProfile;
import com.sagag.services.oates.domain.OatesApplicationEntity;
import com.sagag.services.oates.domain.OatesApplicationIndex;
import com.sagag.services.oates.domain.OatesApplicationRefItem;
import com.sagag.services.oates.domain.OatesDecisionTree;
import com.sagag.services.oates.domain.OatesEquipment;
import com.sagag.services.oates.domain.OatesEquipmentProducts;
import com.sagag.services.oates.domain.OatesResource;
import com.sagag.services.oates.dto.OatesAppNoteDto;
import com.sagag.services.oates.dto.OatesApplicationDto;
import com.sagag.services.oates.dto.OatesChangeIntervalDto;
import com.sagag.services.oates.dto.OatesDecisionTreeDto;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;

@Component
@OatesProfile
public class OatesApplicationConverter
    implements Function<OatesEquipmentProducts, OatesEquipmentProductsDto> {

  @LogExecutionTime("OatesApplicationConverter -> apply -> Convert OATES response in {} ms")
  @Override
  public OatesEquipmentProductsDto apply(OatesEquipmentProducts equipmentProducts) {
    if (equipmentProducts == null || !equipmentProducts.isValidOatesEquipmentProducts()) {
      return new OatesEquipmentProductsDto();
    }
    final OatesEquipment equipment = equipmentProducts.getEquipment();
    final OatesApplicationIndex applicationIndex = equipment.getApplicationIndex();
    if (applicationIndex == null) {
      return new OatesEquipmentProductsDto();
    }

    final List<OatesApplicationRefItem> appRefs = applicationIndex.getAppRef();
    final List<OatesDecisionTreeDto> dTrees = convertOatesDecisionTreeMap(appRefs);

    final List<OatesApplicationEntity> filteredApplications = equipment.getApplications().stream()
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(filteredApplications)) {
      return new OatesEquipmentProductsDto();
    }
    final List<OatesAppNoteDto> appNotes = CollectionUtils.emptyIfNull(equipment.getAppNote())
        .stream().map(appNote -> SagBeanUtils.map(appNote, OatesAppNoteDto.class))
        .collect(Collectors.toList());

    final List<OatesChangeIntervalDto> changesIntervals = CollectionUtils
        .emptyIfNull(equipment.getChangeIntervals()).stream()
        .map(item -> SagBeanUtils.map(item, OatesChangeIntervalDto.class))
        .collect(Collectors.toList());

    final List<OatesApplicationDto> applicationDtos = new ArrayList<>();
    for (OatesApplicationEntity application : filteredApplications) {
      applicationDtos.add(applicationConverter(appNotes, changesIntervals).apply(application));
    }

    return new OatesEquipmentProductsDto(applicationDtos, dTrees);
  }

  private List<OatesDecisionTreeDto> convertOatesDecisionTreeMap(
      final List<OatesApplicationRefItem> appRefs) {
    if (CollectionUtils.isEmpty(appRefs)) {
      return Collections.emptyList();
    }
    List<OatesDecisionTreeDto> dTrees = new ArrayList<>();
    OatesDecisionTree dTree;
    OatesDecisionTreeDto dTreeDto;
    for (OatesApplicationRefItem item : appRefs) {
      dTree = item.getDecisionTree();
      if (dTree == null) {
        continue;
      }
      dTreeDto = SagBeanUtils.map(dTree, OatesDecisionTreeDto.class);
      dTreeDto.setDisplayName(item.getDisplayName());
      dTrees.add(dTreeDto);
    }
    return dTrees;
  }

  private static Function<OatesApplicationEntity, OatesApplicationDto> applicationConverter(
      final List<OatesAppNoteDto> appNotes, final List<OatesChangeIntervalDto> changesIntervals) {
    return application -> {

      OatesApplicationDto applicationDto = SagBeanUtils.map(application, OatesApplicationDto.class);
      applicationDto.setAppNotes(appNotes);
      applicationDto.setChangeIntervals(changesIntervals);

      if (CollectionUtils.isEmpty(application.getProducts())) {
        return applicationDto;
      }
      final List<String> axIds = application.getProducts().stream()
          .filter(oProduct -> CollectionUtils.isNotEmpty(oProduct.getResources()))
          .flatMap(oProduct -> oProduct.getResources().stream())
          .map(OatesResource::getHref)
          .filter(MapUtils::isNotEmpty)
          .flatMap(hrefMap -> hrefMap.values().stream())
          .collect(Collectors.toList());
      applicationDto.setAxIds(axIds);
      return applicationDto;
    };
  }

}
