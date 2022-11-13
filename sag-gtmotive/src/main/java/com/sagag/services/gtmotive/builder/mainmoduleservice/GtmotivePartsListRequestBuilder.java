package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;

public class GtmotivePartsListRequestBuilder extends AbstractGtmotiveMainModuleServiceBuilder {

  private GtmotivePartsListSearchCriteria criteria;

  public GtmotivePartsListRequestBuilder criteria(GtmotivePartsListSearchCriteria criteria) {
    this.criteria = criteria;
    return this;
  }

  @Override
  protected StringBuilder beginSoapBody() {
    return new StringBuilder("<soapenv:Body><gtm:PartsList><gtm:strXmlRequest>");
  }

  @Override
  protected StringBuilder endSoapBody() {
    return new StringBuilder("</gtm:strXmlRequest></gtm:PartsList></soapenv:Body>");
  }

  private StringBuilder requestInfo(GtmotivePartsListSearchRequest searchRequest) {
    StringBuilder contents = new StringBuilder();
    contents.append("<requestInfo>");
    contents.append(vehicleInfo(searchRequest.getUmc(), searchRequest.getEquipments(),
        searchRequest.getEquipmentRanks()));
    contents.append("</requestInfo>");
    return contents;
  }

  private StringBuilder soapContents(GtmotivePartsListSearchCriteria criteria) {
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
