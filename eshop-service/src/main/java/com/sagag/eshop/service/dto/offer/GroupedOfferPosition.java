package com.sagag.eshop.service.dto.offer;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
public class GroupedOfferPosition implements Serializable {

  private static final long serialVersionUID = -1425321868659373419L;

  private final String name;

  private String vehicleId;

  private Long vehicleIdSag;

  private String modelId;

  private Map<GroupedOfferPositionKey, List<OfferPositionDto>> offerPositions =
      new TreeMap<>(new OfferArticleComparator());

  public void addOfferPosition(final OfferPositionDto offerPosition) {
    Assert.notNull(offerPosition, "The given offer position must not be null");
    final String catalogPath = StringUtils.defaultString(offerPosition.getCatalogPath());
    String vehBomDesc = StringUtils.defaultString(offerPosition.getVehicleBomDescription());

    if (StringUtils.isBlank(catalogPath) && StringUtils.isBlank(vehBomDesc)
        && existsVehicleInfo(offerPosition)) {
      vehBomDesc = StringUtils.EMPTY;
    }

    final GroupedOfferPositionKey key = new GroupedOfferPositionKey(vehBomDesc, catalogPath);
    if (!offerPositions.containsKey(key)) {
      offerPositions.put(key, new SortedOfferPositionArrayList());
    }

    offerPositions.get(key).add(offerPosition);
  }

  private static boolean existsVehicleInfo(final OfferPositionDto offerPosition) {
    return !StringUtils.isBlank(offerPosition.getVehicleId())
        || !StringUtils.isBlank(offerPosition.getMakeId())
        || !StringUtils.isBlank(offerPosition.getModelId());
  }

}
