package com.sagag.services.gtmotive.builder;

import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;

import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class GraphicalVehicleRequestBuilder extends AbstractGraphicalRequestBuilder {

  @Setter
  private GtmotiveCriteria criteria;

  public String buildVehicleInfoRequest() {
    return build(true);
  }

  private String build(boolean readMode) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.hasText(criteria.getEstimateId(), "The given estimateId must not be empty");
    final StringBuilder xmlBuilder = new StringBuilder();
    xmlBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gtm=\"http://gtmotive.com/\">");
    xmlBuilder.append("<soapenv:Header/>");
    xmlBuilder.append("<soapenv:Body><gtm:GTIService><gtm:GtRequestXml>");
    xmlBuilder.append(contentsBuilder(criteria, readMode));
    xmlBuilder.append("</gtm:GtRequestXml></gtm:GTIService></soapenv:Body>");
    xmlBuilder.append("</soapenv:Envelope>");
    return xmlBuilder.toString();
  }

  private static String contentsBuilder(GtmotiveCriteria criteria, boolean readMode) {
    final StringBuilder contentBuilder = new StringBuilder();
    contentBuilder.append("<![CDATA[<?xml version=\"1.0\" encoding=\"utf-16\"?>");
    contentBuilder.append("<request>");

    contentBuilder.append(authenticationData(criteria));
    contentBuilder.append("<requestInfo>");
    contentBuilder.append(estimate(criteria, readMode));
    contentBuilder.append("</requestInfo>");

    contentBuilder.append("</request>");
    contentBuilder.append("]]>");
    return contentBuilder.toString();
  }

  private static String authenticationData(GtmotiveCriteria criteria) {
    final StringBuilder authBuilder = new StringBuilder();
    authBuilder.append("<autenticationData>");
    authBuilder.append("<gsId value=\"").append(criteria.getGsId()).append("\"/>");
    authBuilder.append("<gsPwd value=\"").append(criteria.getGsPwd()).append("\"/>");
    authBuilder.append("<customerId value=\"").append(criteria.getCustomerId()).append("\"/>");
    authBuilder.append("<userId value=\"").append(criteria.getUserId()).append("\"/>");
    authBuilder.append("</autenticationData>");
    return authBuilder.toString();
  }

  private static String estimate(GtmotiveCriteria criteria, boolean readMode) {
    final StringBuilder estimateBuilder = new StringBuilder();
    estimateBuilder.append("<estimate>");
    estimateBuilder.append("<operation value=\"").append(readMode ? "read" : "create").append("\"/>");
    estimateBuilder.append("<showGui value=\"false\" multipleUmcsDecoded=\"2\"/>");
    estimateBuilder.append("<estimateInfo>");
    estimateBuilder.append("<estimateId value=\"").append(criteria.getEstimateId()).append("\"/>");
    estimateBuilder.append(vehicleInfo(criteria, criteria.getRegistrationNumber()));
    estimateBuilder.append("</estimateInfo>");
    estimateBuilder.append("</estimate>");
    return estimateBuilder.toString();
  }

  private static String vehicleInfo(GtmotiveCriteria criteria, String registrationNumber) {
    final StringBuilder vehBuilder = new StringBuilder("<vehicleInfo>");

    if (!StringUtils.isBlank(criteria.getModifiedVin())) {
      vehBuilder.append("<registrationNumber value=\"").append(registrationNumber).append(ATTR_LOCKED_FALSE);
      vehBuilder.append("<vin value=\"").append(criteria.getModifiedVin()).append(ATTR_LOCKED_FALSE);
    } else {
      vehBuilder.append("<umc value=\"").append(criteria.getUmc()).append(ATTR_LOCKED_FALSE);
      vehBuilder.append("<registrationNumber value=\"").append(registrationNumber).append(ATTR_LOCKED_FALSE);
      vehBuilder.append("<equipmentList>");
      vehBuilder.append("<items>");
      if (!StringUtils.isBlank(criteria.getGtMod())) {
        vehBuilder.append(ELE_ITEM_OPEN).append(criteria.getGtMod()).append(ELE_ITEM_CLOSE);
      }
      if (!StringUtils.isBlank(criteria.getGtEng())) {
        vehBuilder.append(ELE_ITEM_OPEN).append(criteria.getGtEng()).append(ELE_ITEM_CLOSE);
      }
      if (!StringUtils.isBlank(criteria.getGtDrv())) {
        vehBuilder.append(ELE_ITEM_OPEN).append(criteria.getGtDrv()).append(ELE_ITEM_CLOSE);
      }
      vehBuilder.append("</items>");
      vehBuilder.append("</equipmentList>");
    }
    vehBuilder.append("</vehicleInfo>");
    return vehBuilder.toString();
  }

}
