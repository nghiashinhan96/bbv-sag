package com.sagag.services.stakis.erp.enums;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StakisContactType {

  PHONE(new int[] { 1, 2 }), FAX(new int[] { 3 }), EMAIL(new int[] { 4 });

  private int[] types;

  public static Optional<StakisContactType> fromType(int type) {
    return Stream.of(values()).filter(value -> ArrayUtils.contains(value.types, type)).findFirst();
  }

}
