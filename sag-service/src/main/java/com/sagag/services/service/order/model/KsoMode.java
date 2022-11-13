package com.sagag.services.service.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Getter
public enum KsoMode {
  NOT_EFFECT(null), ON(true), OFF(false);

  private Boolean flag;

  public static KsoMode findByFlag(Boolean flag) {
    return Arrays.asList(KsoMode.values()).stream().filter(val -> val.getFlag() == flag).findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            String.format("No support the given kso flag %s", flag)));
  }

}
