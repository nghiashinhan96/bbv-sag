package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IndexFieldType {
  NESTED("nested"),
  NONE_NESTED("non_nested");

  private final String value;

  public String value() {
    return this.value;
  }
}
