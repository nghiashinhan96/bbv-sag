package com.sagag.services.elasticsearch.enums;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;


@AllArgsConstructor
public enum ArticleField implements IAttributePath {

  ID(StringUtils.EMPTY, "artid"),
  CRITERIA(StringUtils.EMPTY, "criteria"),
  REFS(StringUtils.EMPTY, "refs"),
  ID_SAGSYS(StringUtils.EMPTY, "id_pim"),
  ID_AUTONET(StringUtils.EMPTY, "id_autonet"),
  SUPPLIER(StringUtils.EMPTY, "supplier"),
  SUPPLIER_RAW(StringUtils.EMPTY, "supplier.raw"),
  GA_ID(StringUtils.EMPTY, "gaID"),
  ART_NUMBER(StringUtils.EMPTY, "artnr"),
  ID_DLNR(StringUtils.EMPTY, "id_dlnr"),
  ARTNR_DISPLAY(StringUtils.EMPTY, "artnr_display"),
  PARTS(StringUtils.EMPTY, "parts"),
  PARTS_EXT(StringUtils.EMPTY, "parts_ext"),
  PARTS_EXT_NUMBER(PARTS_EXT.value(), "parts_ext.pnrn"),
  PARTS_NUMBER(PARTS.value(), "parts.pnrn"),
  PART_PTYPE(PARTS.value(), "parts.ptype"),
  INFO(StringUtils.EMPTY, "info"),
  ID_UMSART(StringUtils.EMPTY, "id_umsart"),
  CRITERIA_CID(CRITERIA.value(), "criteria.cid"),
  CRITERIA_CVP(CRITERIA.value(), "criteria.cvp"),
  CRITERIA_CVP_RAW(StringUtils.EMPTY, "criteria.cvp.raw"),
  CRITERIA_CN(CRITERIA.value(), "criteria.cn"),
  SAG_PRODUCT_GROUP(StringUtils.EMPTY, "sag_product_group"),
  LOCKS(StringUtils.EMPTY, "locks"),
  COUNTRY_EXCL(StringUtils.EMPTY, "country_excl"),
  LOCKS_FRONT_END(StringUtils.EMPTY, "locks_front_end"),
  NAME(StringUtils.EMPTY, "name"),
  PRODUCT_BRAND(StringUtils.EMPTY, "product_brand"),
  PRODUCT_ADDON(StringUtils.EMPTY, "product_addon"),
  LOCKS_VK(StringUtils.EMPTY, "locks_vk"),
  LOCKS_KU(StringUtils.EMPTY, "locks_ku"),
  PRODUCT_GROUP(StringUtils.EMPTY, "sag_product_group"),
  PRODUCT_GROUP_2(StringUtils.EMPTY, "sag_product_group_2"),
  PRODUCT_GROUP_3(StringUtils.EMPTY, "sag_product_group_3"),
  PRODUCT_GROUP_4(StringUtils.EMPTY, "sag_product_group_4"),
  ID_PRODUCT_BRAND(StringUtils.EMPTY, "id_product_brand"),
  ID_GOLDEN_RECORD(StringUtils.EMPTY, "id_gr"),
  BOM(StringUtils.EMPTY, "bom"),
  PSEUDO(StringUtils.EMPTY, "pseudo");


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

  public static ArticleField valueOfArticleIdField(String value) {
    if (StringUtils.equalsIgnoreCase(ArticleField.ID_AUTONET.value(), value)) {
      return ArticleField.ID_AUTONET;
    }
    return ArticleField.ID_SAGSYS;
  }
}
