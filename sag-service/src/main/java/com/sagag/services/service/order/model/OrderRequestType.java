package com.sagag.services.service.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Order OrderType that can be used.
 *
 */
@AllArgsConstructor
@Getter
public enum OrderRequestType {
  PLACE_ORDER("ORDER"),
  TRANSFER_BASKET("ORDER"),
  CREATE_OFFER("OFFER"),
  COUNTER_TRANSFER_BASKET("COUNTER"),
  ABS_ORDER("ABS"),
  STD_ORDER("STD"),
  KSO_AUT("KSO_AUT"),
  KSO_AUT_TRANSFER_BASKET("KSO_AUT"),
  KSO_AUT_CREATE_OFFER("KSO_AUT");

  private String jsonEventType;

  public static OrderRequestType fromName(final String name) {
    return Arrays.asList(values()).stream()
        .filter(val -> StringUtils.equals(name, val.name()))
        .findFirst().orElseThrow(() -> new NoSuchElementException(
            String.format("No support the given OrderRequestType %s", name)));
  }
}
