package com.sagag.services.ivds.freetext;

import org.apache.commons.lang3.StringUtils;

public enum SearchOptions {

  ARTICLES, VEHICLES, PRODUCT_CATEGORY;

  public String lowerCase() {
    return StringUtils.lowerCase(this.name());
  }
}
