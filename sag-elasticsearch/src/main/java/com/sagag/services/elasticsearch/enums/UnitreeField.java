package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;

import org.apache.commons.lang.StringUtils;

@AllArgsConstructor
public enum UnitreeField implements IAttributePath {

  NODES(StringUtils.EMPTY, "nodes"),
  NODE_NAME(NODES.value(), "node_name"),
  NODE_KEYWORDS(NODES.value(), "node_keywords");

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
    return this.value;
  }

  @Override
  public String path() {
    return this.code;
  }

  @Override
  public String aggTerms() {
    return this.name();
  }

}
