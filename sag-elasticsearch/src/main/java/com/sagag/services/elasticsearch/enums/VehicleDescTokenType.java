package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VehicleDescTokenType {
  NUMERIC_PS, ALPHANUMERIC_PS, NUMERIC, ALPHABET, NUMERIC_KW, ALPHANUMERIC_KW;
}
