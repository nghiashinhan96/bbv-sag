package com.sagag.services.tools.support;

import org.springframework.stereotype.Component;

@Component
public class ConnectQueryFactory {

  public StringBuilder createOrganisationHasOfferPermissionQueryBuilder() {
    final StringBuilder queryBuilder = new StringBuilder()
      .append("SELECT DISTINCT org.* ")
      .append("FROM dbo.ORGANISATION org ")
      .append("INNER JOIN dbo.ORGANISATION_GROUP og ON og.ORGANISATION_ID=org.ID ")
      .append("INNER JOIN dbo.GROUP_PERMISSION gp ON gp.GROUP_ID = og.GROUP_ID ")
      .append("INNER JOIN dbo.ESHOP_PERMISSION ep ON ep.ID = gp.PERM_ID ")
      .append("INNER JOIN dbo.PERM_FUNCTION pf ON pf.PERM_ID = ep.ID ")
      .append("INNER JOIN dbo.ESHOP_FUNCTION ef ON ef.ID = pf.FUNCTION_ID ")
      .append("WHERE org.ORG_CODE IN (:orgCodeList) AND ef.RELATIVE_URL= :relativeUrl AND gp.ALLOWED= :allowed");
    return queryBuilder;
  }

  public String createFindOfferPositionsByVendorIdListQuery() {
    return "select op from SourceOfferPosition op inner join op.offer o where o.vendorId IN :vendorIds";
  }

  public String createFindOffersByVendorIdListQuery() {
    return "select op from SourceOffer op where op.vendorId IN :vendorIds";
  }

}
