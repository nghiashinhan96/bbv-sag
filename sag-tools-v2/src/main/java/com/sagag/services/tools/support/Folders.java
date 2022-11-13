package com.sagag.services.tools.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.SystemUtils;

@AllArgsConstructor
@Getter
public enum Folders {

  EXPORT_PERSON_DIR_PATH(SystemUtils.getUserDir() + "/csv/export/person/"),
  EXPORT_PERSON_PROPERTY_DIR_PATH(SystemUtils.getUserDir() + "/csv/export/person_property/"),
  EXPORT_SHOP_ARTICLE_DIR_PATH(SystemUtils.getUserDir() + "/csv/export/shop_article/"),
  EXPORT_FINAL_CUSTOMER_DIR_PATH(SystemUtils.getUserDir() + "/csv/export/final_customer/");

  private String path;
}
