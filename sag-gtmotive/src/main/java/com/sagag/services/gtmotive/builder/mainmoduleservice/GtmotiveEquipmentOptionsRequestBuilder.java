package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class GtmotiveEquipmentOptionsRequestBuilder
    extends AbstractGtmotiveMainModuleServiceBuilder {

  private GtmotiveEquipmentOptionsSearchCriteria criteria;

  public GtmotiveEquipmentOptionsRequestBuilder criteria(
      GtmotiveEquipmentOptionsSearchCriteria criteria) {
    this.criteria = criteria;
    return this;
  }

  @Override
  protected StringBuilder beginSoapBody() {
    return new StringBuilder("<soapenv:Body><gtm:EquipmentOptions><gtm:strXmlRequest>");
  }

  @Override
  protected StringBuilder endSoapBody() {
    return new StringBuilder("</gtm:strXmlRequest></gtm:EquipmentOptions></soapenv:Body>");
  }


  private StringBuilder requestInfo(GtmotiveEquipmentOptionsSearchRequest searchRequest) {
    StringBuilder contents = new StringBuilder();
    contents.append("<requestInfo>");
    contents.append(vehicleInfo(searchRequest.getUmc()));

    List<String> partCodes = searchRequest.getPartCodes();
    if (CollectionUtils.isNotEmpty(partCodes)) {
      contents.append(partCodeList(partCodes));
    }

    String functionalGroup = searchRequest.getFunctionalGroup();
    if (StringUtils.isNotBlank(functionalGroup)) {
      contents.append(functionalGroup(functionalGroup));
    }
    contents.append("</requestInfo>");
    return contents;
  }

  private StringBuilder vehicleInfo(String umc) {
    StringBuilder contents = new StringBuilder();
    contents.append("<vehicleInfo>");
    contents.append("<umc>");
    contents.append(umc);
    contents.append("</umc>");
    contents.append("</vehicleInfo>");
    return contents;
  }

  private StringBuilder partCodeList(List<String> partCodes) {
    StringBuilder contents = new StringBuilder();
    contents.append("<partCodeList>");
    partCodes.stream().forEach(value -> contents.append(partCode(value)));
    contents.append("</partCodeList>");
    return contents;
  }

  private StringBuilder functionalGroup(String functionalGroup) {
    StringBuilder contents = new StringBuilder();
    contents.append("<functionalGroup>");
    contents.append(functionalGroup);
    contents.append("</functionalGroup>");
    return contents;
  }

  private StringBuilder soapContents(GtmotiveEquipmentOptionsSearchCriteria criteria) {
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
