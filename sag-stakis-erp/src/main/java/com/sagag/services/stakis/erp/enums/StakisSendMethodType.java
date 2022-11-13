package com.sagag.services.stakis.erp.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sagag.services.common.enums.SendMethodType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StakisSendMethodType {

  SELF(SendMethodType.PICKUP, 2), NORM(SendMethodType.TOUR, 1);

  private SendMethodType sendMethod;

  private int id;

  private static final Map<SendMethodType, StakisSendMethodType> map;
  static {
    map = new HashMap<SendMethodType, StakisSendMethodType>();
    Arrays.asList(values()).forEach(s -> {
      map.put(s.sendMethod, s);
    });
  }

  public static StakisSendMethodType findBySendMethod(SendMethodType smt) {
    return Optional.ofNullable(map.get(smt)).orElseThrow(
        () -> new IllegalArgumentException("Not support this type for stakis send method"));
  }
}
