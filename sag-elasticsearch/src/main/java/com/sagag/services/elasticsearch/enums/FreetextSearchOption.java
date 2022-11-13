package com.sagag.services.elasticsearch.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Freetext search options.
 */
public enum FreetextSearchOption {
  ARTICLES, VEHICLES, ARTICLES_DESC, VEHICLES_MOTOR;
  
  /**
   * Lowercases the enumeration name.
   */
  public String lowerCase() {
    return StringUtils.lowerCase(this.name());
  }

  /**
   * Checks if the search option is the vehicle mortor and its description.
   */
  public boolean isVehiclesMotor() {
    return this == VEHICLES_MOTOR;
  }

  /**
   * Checks if the search option is the article description.
   */
  public boolean isArticleDesc() {
    return this == ARTICLES_DESC;
  }
  
}
