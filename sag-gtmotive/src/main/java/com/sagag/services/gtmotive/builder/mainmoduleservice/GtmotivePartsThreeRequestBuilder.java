package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;

public class GtmotivePartsThreeRequestBuilder extends AbstractGtmotiveMainModuleServiceBuilder {

  private GtmotivePartsThreeSearchCriteria criteria;

  public GtmotivePartsThreeRequestBuilder criteria(GtmotivePartsThreeSearchCriteria criteria) {
    this.criteria = criteria;
    return this;
  }

  @Override
  protected StringBuilder beginSoapBody() {
    return new StringBuilder("<soapenv:Body><gtm:PartsThree><gtm:strXmlRequest>");
  }

  @Override
  protected StringBuilder endSoapBody() {
    return new StringBuilder("</gtm:strXmlRequest></gtm:PartsThree></soapenv:Body>");
  }

  private StringBuilder requestInfo(GtmotivePartsThreeSearchRequest searchRequest) {
    StringBuilder contents = new StringBuilder();
    contents.append("<requestInfo>");
    contents.append(operationInfo(searchRequest.getPartCode()));
    contents.append(vehicleInfo(searchRequest.getUmc(), searchRequest.getEquipments(),
        searchRequest.getEquipmentRanks()));
    contents.append("</requestInfo>");
    return contents;
  }

  private StringBuilder soapContents(GtmotivePartsThreeSearchCriteria criteria) {
    final StringBuilder contents =
        new StringBuilder("<![CDATA[<?xml version=\"1.0\" encoding=\"utf-16\"?>");
    contents.append("<request>");
    contents.append(authenticationData(criteria.getAuthenData()));
    contents.append(requestInfo(criteria.getSearchRequest()));
    contents.append("</request>");
    contents.append("]]>");
    return contents;
  }

  //@formatter:off
  public String build() {
    return beginSoapEnvelope()
              .append(soapHeader())
              .append(beginSoapBody())
                  .append(soapContents(this.criteria))
              .append(endSoapBody())
            .append(endSoapEnvelope()).toString();
  }
  //@formatter:off
}
