package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.builder.gtinterface.AbstractGtmotiveRequestBuilder;
import com.sagag.services.gtmotive.domain.request.AuthenticationData;
import com.sagag.services.gtmotive.domain.response.EquipmentRank;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public abstract class AbstractGtmotiveMainModuleServiceBuilder
    extends AbstractGtmotiveRequestBuilder {

  protected StringBuilder beginSoapEnvelope() {
    return new StringBuilder(
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gtm=\"GtMotive.WSGTI.DistributedServices.WSGtMainModuleService\">");
  }

  protected StringBuilder authenticationData(AuthenticationData authenData) {
    StringBuilder contents = new StringBuilder();
    contents.append("<autenticationData>");
    contents.append("<gsId>").append(authenData.getGsId()).append("</gsId>");
    contents.append("<gsPwd>").append(authenData.getGsPwd()).append("</gsPwd>");
    contents.append("<customerId>").append(authenData.getCustomerId()).append("</customerId>");
    contents.append("<userId>").append(authenData.getUserId()).append("</userId>");
    contents.append("</autenticationData>");
    return contents;
  }

  protected abstract StringBuilder beginSoapBody();

  protected abstract StringBuilder endSoapBody();

  //@formatter:off
  protected StringBuilder operationInfo(String partCode) {
    StringBuilder contents = new StringBuilder();
    contents.append("<operationInfo>");
      contents.append("<partCode>");
        contents.append(partCode);
      contents.append("</partCode>");
    contents.append("</operationInfo>");
    return contents;
  }
  //@formatter:on

  protected StringBuilder partCode(String partCode) {
    StringBuilder contents = new StringBuilder();
    contents.append("<partCode>");
    contents.append(partCode);
    contents.append("</partCode>");
    return contents;
  }

  protected StringBuilder vehicleInfo(String umc, List<String> equipmentCodes,
      List<EquipmentRank> ranks) {
    StringBuilder contents = new StringBuilder();
    contents.append("<vehicleInfo>");
    contents.append("<umc>");
    contents.append(umc);
    contents.append("</umc>");
    if (!CollectionUtils.isEmpty(equipmentCodes) || !CollectionUtils.isEmpty(ranks)) {
      contents.append("<equipmentList>");
      if (!CollectionUtils.isEmpty(equipmentCodes)) {
        contents.append(items(equipmentCodes));
      }
      if (!CollectionUtils.isEmpty(ranks)) {
        contents.append(ranks(ranks));
      }
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

  private StringBuilder ranks(List<EquipmentRank> ranks) {
    StringBuilder contents = new StringBuilder();
    contents.append("<ranks>");
    ranks.stream().forEach(rank -> contents.append(rank(rank)));
    contents.append("</ranks>");
    return contents;
  }

  private StringBuilder rank(EquipmentRank rank) {
    StringBuilder contents = new StringBuilder();
    contents.append("<rank>");
    contents.append("<family>").append(rank.getFamily()).append("</family>");
    contents.append("<subfamily>").append(rank.getSubFamily()).append("</subfamily>");
    contents.append("<value>").append(rank.getValue()).append("</value>");
    contents.append("</rank>");
    return contents;
  }
}
