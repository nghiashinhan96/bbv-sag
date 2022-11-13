package com.sagag.eshop.service.dto.offer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Comparator;

public class OfferArticleComparator implements Comparator<GroupedOfferPositionKey>, Serializable {

  private static final long serialVersionUID = -2974378304160931973L;

  @Override
  public int compare(GroupedOfferPositionKey key1, GroupedOfferPositionKey key2) {

    if (key1 == null && key2 == null) {
      return NumberUtils.INTEGER_ZERO;
    } else if (key1 != null && key2 == null) {
      return NumberUtils.INTEGER_MINUS_ONE;
    } else if (key1 == null) {
      return NumberUtils.INTEGER_ONE;
    }

    final String catalogPath1 = key1.getCatalogPath();
    final String catalogPath2 = key2.getCatalogPath();
    if (StringUtils.isBlank(catalogPath1) && StringUtils.isBlank(catalogPath2)) {
      return StringUtils.defaultString(key1.getVehicleBomDescription())
          .compareToIgnoreCase(StringUtils.defaultString(key2.getVehicleBomDescription()));
    } else if (StringUtils.isNotBlank(catalogPath1) && StringUtils.isBlank(catalogPath2)) {
      return NumberUtils.INTEGER_MINUS_ONE;
    } else if (StringUtils.isBlank(catalogPath1) && StringUtils.isNotBlank(catalogPath2)) {
      return NumberUtils.INTEGER_ONE;
    }
    return StringUtils.defaultString(catalogPath1).compareToIgnoreCase(
        StringUtils.defaultString(catalogPath2));
  }

}
