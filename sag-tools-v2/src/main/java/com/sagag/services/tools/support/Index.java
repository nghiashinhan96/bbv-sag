package com.sagag.services.tools.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fields declaration interface for Elasticsearch query.
 */
public interface Index {

  /**
   * Branch enumeration for indexes.
   */
  @Getter
  @AllArgsConstructor
  enum Branch {
    //@formatter:off
    ID("id"),
    ZIP("zip"),
    ADDRESS_DESC("address_desc"),
    ADDRESS_COUNTRY("address_country"),
    ADDRESS_STREET("address_street"),
    REGION_ID("region_id"),
    PRIMARY_EMAIL("primary_email"),
    BRANCH_NR("branch_nr"),
    PRIMARY_URL("primary_url"),
    PRIMARY_FAX("primary_fax"),
    ADDRESS_CITY("address_city"),
    PRIMARY_PHONE("primary_phone"),
    ORG_ID("org_id");
    //@formatter:on

    private final String value;

  }
}

