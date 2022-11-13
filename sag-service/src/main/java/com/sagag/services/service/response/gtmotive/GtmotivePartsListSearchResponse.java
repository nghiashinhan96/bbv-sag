package com.sagag.services.service.response.gtmotive;

import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePart;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.service.response.GtmotiveFunctionalGroupDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GtmotivePartsListSearchResponse implements Serializable {

  private static final long serialVersionUID = -175164515417252539L;

  private List<GtmotiveFunctionalGroupDto> functionalGroups;

  public GtmotivePartsListSearchResponse(GtmotivePartsListResponse response) {
    final Map<GtmotiveFunctionalGroup, List<GtmotivePartItem>> partListResult =
        collectPartsListResult(response.getParts());

    if (partListResult.isEmpty()) {
      return;
    }
    setFunctionalGroups(partListResult.entrySet().stream()
        .map(gtmotiveFunctionalConverter()).collect(Collectors.toList()));
  }

  private static Map<GtmotiveFunctionalGroup, List<GtmotivePartItem>> collectPartsListResult(
      List<GtmotivePart> parts) {
    List<GtmotivePart> validParts = CollectionUtils.emptyIfNull(parts).stream()
        .filter(GtmotivePart::isValidItem).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(validParts)) {
      return Collections.emptyMap();
    }
    Map<GtmotiveFunctionalGroup, List<GtmotivePartItem>> map =
        validParts.stream().collect(Collectors.groupingBy(collectFunctionGroup(),
            sortedFunctionalGroupTreeMap(), collectPartItems()));
    map.forEach((key, values) -> values
        .sort((a, b) -> a.getPartDescription().compareToIgnoreCase(b.getPartDescription())));
    return map;
  }

  private static Function<GtmotivePart, GtmotiveFunctionalGroup> collectFunctionGroup() {
    return part -> GtmotiveFunctionalGroup.builder().functionalGroup(part.getFunctionalGroup())
        .functionalGroupDescription(part.getFunctionalGroupDescription()).build();
  }

  private static Collector<GtmotivePart, ?, List<GtmotivePartItem>> collectPartItems() {
    return Collectors.mapping(toPartItem(), Collectors.toList());
  }

  private static Function<GtmotivePart, GtmotivePartItem> toPartItem() {
    return part -> GtmotivePartItem.builder().partCode(part.getPartCode())
        .partDescription(part.getPartDescription()).build();
  }

  private static Supplier<Map<GtmotiveFunctionalGroup,
    List<GtmotivePartItem>>> sortedFunctionalGroupTreeMap() {
    return () -> new TreeMap<>((a, b) -> a.getFunctionalGroupDescription()
        .compareToIgnoreCase(b.getFunctionalGroupDescription()));
  }

  private Function<Entry<GtmotiveFunctionalGroup, List<GtmotivePartItem>>,
    GtmotiveFunctionalGroupDto> gtmotiveFunctionalConverter() {
  return item -> {
    GtmotiveFunctionalGroup group = item.getKey();
    return GtmotiveFunctionalGroupDto.builder().functionalGroup(group.getFunctionalGroup())
        .functionalGroupDescription(group.getFunctionalGroupDescription()).parts(item.getValue())
        .build();
  };
}
}
