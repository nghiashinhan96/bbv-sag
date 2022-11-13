package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum AllDataArticleField implements IAttributePath {

  ARTICLE_NUMBER(StringUtils.EMPTY, "Artikelnummer"),
  USAGE_NUMBER(StringUtils.EMPTY, "Gebrauchsnummer"),
  OE_NUMBER(StringUtils.EMPTY, "oe_number"),
  REFERENCE_NUMBER(StringUtils.EMPTY, "reference_number"),
  EAN_NUMBER(StringUtils.EMPTY, "EAN");

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
