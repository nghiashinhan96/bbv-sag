package com.sagag.services.common.enums;


public enum OrganisationTypeEnum {
  SYSTEM("This is SAG System organisation"), AFFILIATE(
      "This is Affiliate organisation (Normally SAG)"), CUSTOMER(
          "This is Customer organisation"), FINAL_CUSTOMER("This is final customer organisation");

  private final String organisationTypeName;

  OrganisationTypeEnum(String organisationName) {
    this.organisationTypeName = organisationName;
  }

  public String getOrganisationTypeName() {
    return organisationTypeName;
  }

  @Override
  public String toString() {
    return getOrganisationTypeName();
  }
}
