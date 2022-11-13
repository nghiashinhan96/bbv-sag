package com.sagag.services.gtmotive.builder.gtinterface;

import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartInfoRequest;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class GtmotivePartInfoRequestBuilder extends AbstractGtInterfaceRequestBuilder {

  private GtmotivePartInfoCriteria criteria;

  public GtmotivePartInfoRequestBuilder criteria(GtmotivePartInfoCriteria criteria) {
    this.criteria = criteria;
    return this;
  }

  private StringBuilder soapContents(GtmotivePartInfoCriteria criteria) {
    final StringBuilder contents =
        new StringBuilder("<![CDATA[<?xml version=\"1.0\" encoding=\"utf-16\"?>");
    contents.append("<request>");
    contents.append(authenticationData(criteria.getAuthenData()));
    contents.append(requestInfo(criteria.getSearchRequest()));
    contents.append("</request>");
    contents.append("]]>");
    return contents;
  }

  private StringBuilder requestInfo(GtmotivePartInfoRequest request) {
    StringBuilder contents = new StringBuilder();
    contents.append("<requestInfo>");
    contents.append(estimate(request.getEstimateId(), request.getUmc(), request.getEquipments()));
    contents.append("</requestInfo>");
    return contents;
  }

  private StringBuilder estimate(String estimateId, String umc, List<String> equipmentCodes) {
    StringBuilder contents = new StringBuilder();
    contents.append("<estimate>");
    contents.append("<operation value=\"read\"/>");
    contents.append("<showGui value=\"false\" multipleUmcsDecoded=\"2\"/>");
    contents.append(estimateInfo(estimateId, umc, equipmentCodes));
    contents.append("</estimate>");
    return contents;
  }

  private StringBuilder estimateInfo(String estimateId, String umc, List<String> equipmentCodes) {
    StringBuilder contents = new StringBuilder();
    contents.append("<estimateInfo>");
    contents.append("<estimateId value=\"").append(estimateId).append(TAG_END);
    contents.append(vehicleInfo(umc, equipmentCodes));
    contents.append("</estimateInfo>");
    return contents;
  }

  private StringBuilder vehicleInfo(String umc, List<String> equipmentCodes) {
    StringBuilder contents = new StringBuilder();
    contents.append("<vehicleInfo>");
    contents.append("<umc value=\"").append(umc).append(ATTR_LOCKED_FALSE);
    contents.append("<registrationNumber value=\"").append(DF_REGISTRATION_NUMBER)
        .append(ATTR_LOCKED_FALSE);
    if (!CollectionUtils.isEmpty(equipmentCodes)) {
      contents.append("<equipmentList>");
      contents.append(items(equipmentCodes));
      contents.append("</equipmentList>");
    }
    contents.append("</vehicleInfo>");
    return contents;
  }

  private StringBuilder items(List<String> values) {
    StringBuilder contents = new StringBuilder();
    contents.append("<items>");
    values.stream().forEach(value -> contents.append(item(value)));
    contents.append("</items>");
    return contents;
  }

  private StringBuilder item(String value) {
    StringBuilder contents = new StringBuilder();
    contents.append("<item>");
    contents.append(value);
    contents.append("</item>");
    return contents;
  }

  //@formatter:off
  public String build() {
    return beginSoapEnvelope()
              .append(beginSoapBody())
                  .append(soapContents(this.criteria))
              .append(endSoapBody())
            .append(endSoapEnvelope()).toString();
  }
  //@formatter:off
}
