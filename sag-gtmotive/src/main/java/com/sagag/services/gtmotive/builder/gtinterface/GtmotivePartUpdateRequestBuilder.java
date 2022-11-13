package com.sagag.services.gtmotive.builder.gtinterface;

import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartUpdateCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartUpdateRequest;

public class GtmotivePartUpdateRequestBuilder extends AbstractGtInterfaceRequestBuilder {

  private GtmotivePartUpdateCriteria criteria;

  public GtmotivePartUpdateRequestBuilder criteria(GtmotivePartUpdateCriteria criteria) {
    this.criteria = criteria;
    return this;
  }

  private StringBuilder soapContents(GtmotivePartUpdateCriteria criteria) {
    final StringBuilder contents = new StringBuilder("<![CDATA[");
    contents.append("<request>");
    contents.append(authenticationData(criteria.getAuthenData()));
    contents.append(requestInfo(criteria.getRequest()));
    contents.append("</request>");
    contents.append("]]>");
    return contents;
  }

  private StringBuilder requestInfo(GtmotivePartUpdateRequest request) {
    StringBuilder contents = new StringBuilder();
    contents.append("<requestInfo>");
    contents.append(estimate(request.getEstimateId(), request.getShortNumber()));
    contents.append("</requestInfo>");
    return contents;
  }

  private StringBuilder estimate(String estimateId, String shortNumber) {
    StringBuilder contents = new StringBuilder();
    contents.append("<estimate>");
    contents.append("<operation value=\"update\"/>");
    contents.append("<showGui value=\"false\"/>");
    contents.append(estimateInfo(estimateId, shortNumber));
    contents.append(behaviour());
    contents.append("</estimate>");
    return contents;
  }

  private StringBuilder estimateInfo(String estimateId, String shortNumber) {
    StringBuilder contents = new StringBuilder();
    contents.append("<estimateInfo>");
    contents.append(estimateId(estimateId));
    contents.append(userData());
    contents.append(operationList(shortNumber));
    contents.append("</estimateInfo>");
    return contents;
  }

  private StringBuilder estimateId(String estimateId) {
    StringBuilder contents = new StringBuilder();
    contents.append("<estimateId value=\"").append(estimateId).append(TAG_END);
    return contents;
  }

  private StringBuilder userData() {
    StringBuilder contents = new StringBuilder();
    contents.append("<userData>");
    contents.append(functionality());
    contents.append("</userData>");
    return contents;
  }

  private StringBuilder functionality() {
    StringBuilder contents = new StringBuilder();
    contents.append("<functionality>");
    contents.append("<hideEconomicParts value=\"true\"/>");
    contents.append("<hideStandardParts value=\"true\"/>");
    contents.append("</functionality>");
    return contents;
  }

  private StringBuilder operationList(String shortNumber) {
    StringBuilder contents = new StringBuilder();
    contents.append("<operationList deleteOperations=\"false\" reset=\"true\">");
    contents.append(activator(shortNumber));
    contents.append("</operationList>");
    return contents;
  }

  private StringBuilder activator(String shortNumber) {
    StringBuilder contents = new StringBuilder();
    contents.append("<activator>");
    contents.append("<shortNumber value=\"").append(shortNumber).append(TAG_END);
    contents.append("<action value=\"SU\"/>");
    contents.append("</activator>");
    return contents;
  }

  private StringBuilder behaviour() {
    StringBuilder contents = new StringBuilder();
    contents.append("<behaviour>");
    contents.append("<omitResult value=\"2\"/>");
    contents.append("</behaviour>");
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
