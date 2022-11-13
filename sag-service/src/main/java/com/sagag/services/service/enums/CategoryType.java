package com.sagag.services.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Enumeration type of categories.
 */
@Getter
@AllArgsConstructor
public enum CategoryType {
  ALL(0), SERVICE(1);

  private int mode;

  public boolean isService() {
    return SERVICE == this;
  }

  /**
   * Checks the any categories type is ignored.
   *
   */
  public boolean isIgnoredOpen() {
    return ALL == this;
  }

  public static CategoryType valueOfMode(final int mode) {
    return Stream.of(values()).filter(val -> val.getMode() == mode).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not support for this category type"));
  }

  public static List<CategoryType> availableValues() {
    return Arrays.asList(ALL);
  }
}
