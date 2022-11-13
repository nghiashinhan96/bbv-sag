package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum LocationType {

  PRIMARY(1), SECONDARY(2), THIRD(3), NON_PRIORITY(0);

  private Integer orderingPriority;

  public static LocationType fromOrderingPriority(Integer orderingPriority) {
    int prio = Objects.isNull(orderingPriority) ? 0 : orderingPriority.intValue();
    return Stream.of(values()).filter(location -> location.getOrderingPriority() == prio)
        .findFirst().orElseGet(() -> LocationType.NON_PRIORITY);
  }

}
