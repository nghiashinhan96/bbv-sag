package com.sagag.services.service.order.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Api type of Order process that can be used.
 *
 */
public enum ApiRequestType {
  ORDER,
  TRANSFER_BASKET,
  OFFER;

  public static ApiRequestType fromName(final String name) {
    return Arrays.asList(values()).stream()
        .filter(val -> StringUtils.equals(name, val.name()))
        .findFirst().orElseThrow(() -> new NoSuchElementException(
            String.format("No support the given order api type %s", name)));
  }
}
