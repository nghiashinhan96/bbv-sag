package com.sagag.eshop.repo.cache;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RepoCacheMaps {

  public static final String RESULT_IS_NULL = "#result == null";

  public static final String ORGANISATIONS_BY_SHORTNAME = "ORGANISATIONS_BY_SHORTNAME";

  public static final String COMPANY_NAMES_BY_SHORTNAME = "COMPANY_NAMES_BY_SHORTNAME";

  public static final String ORGANISATIONS_BY_ID = "ORGANISATIONS_BY_ID";

  public static final String ORGANISATIONS_BY_ORGCODE = "ORGANISATIONS_BY_ORGCODE";

  public static final String IDS_BY_SHORTNAME = "IDS_BY_SHORTNAME";

  public static final String ALL_ORGANISATIONS_BY_ID = "ALL_ORGANISATIONS_BY_ID";

  public static final String ALL_ORGANISATIONS_BY_TYPE = "ALL_ORGANISATIONS_BY_TYPE";

  public static final String EXISTINGS_BY_ORGCODE = "EXISTINGS_BY_ORGCODE";

  public static final String ID_BY_ORGCODE = "ID_BY_ORGCODE";

  public static final String ORGANISATION_COLLECTIONS_SETTINGS_BY_COLL_SHORTNAME =
      "ORGANISATION_COLLECTIONS_SETTINGS_BY_COLL_SHORTNAME";
  
  public static final String LEGAL_DOC_BY_CUSTOMER_ID_AND_LANGUGE_AND_STATUS = "LEGAL_DOC_BY_CUSTOMER_ID_AND_LANGUGE_AND_STATUS";
  
  public String[] getAllMaps() {
    return new String[] {
      ORGANISATIONS_BY_SHORTNAME,
      COMPANY_NAMES_BY_SHORTNAME,
      ORGANISATIONS_BY_ID,
      ORGANISATIONS_BY_ORGCODE,
      IDS_BY_SHORTNAME,
      ALL_ORGANISATIONS_BY_ID,
      ALL_ORGANISATIONS_BY_TYPE,
      ALL_ORGANISATIONS_BY_TYPE,
      EXISTINGS_BY_ORGCODE,
      ID_BY_ORGCODE,
      ORGANISATION_COLLECTIONS_SETTINGS_BY_COLL_SHORTNAME,
      LEGAL_DOC_BY_CUSTOMER_ID_AND_LANGUGE_AND_STATUS
    };
  }
}
