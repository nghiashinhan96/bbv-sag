package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum LicensePlateField implements IAttributePath {

  LP(StringUtils.EMPTY, "lp"),
  SNR(StringUtils.EMPTY, "snr"),
  TSN(StringUtils.EMPTY, "tsn");

  private final String code;

  private final String value;

  public String value() {
    return this.value;
  }

  public String code() {
    return this.code;
  }

  @Override
  public String field() {
    return this.value();
  }

  @Override
  public String path() {
    return this.code();
  }

  @Override
  public String aggTerms() {
    return this.name();
  }
}
