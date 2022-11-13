package com.sagag.services.ax.availability.externalvendor;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.sagag.services.ax.domain.vendor.ExternalStockInfo;

public final class ExternalStockInfoUtil {

  public static final List<ExternalStockInfo> sortByPriority(List<ExternalStockInfo> input) {
    if (CollectionUtils.isEmpty(input)) {
      return input;
    }
    return input.stream().sorted(sortByPriorityComparator()).collect(Collectors.toList());
  }

  public static final Comparator<ExternalStockInfo> sortByPriorityComparator() {
    return (a, b) -> NumberUtils.compare(
        Optional.ofNullable(a.getExternalVendor().getVendorPriority()).orElse(0),
        Optional.ofNullable(b.getExternalVendor().getVendorPriority()).orElse(0));
  }
}
