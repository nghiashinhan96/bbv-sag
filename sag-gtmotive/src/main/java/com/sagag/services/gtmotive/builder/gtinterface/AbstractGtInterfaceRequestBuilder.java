package com.sagag.services.gtmotive.builder.gtinterface;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;

public abstract class AbstractGtInterfaceRequestBuilder extends AbstractGtmotiveRequestBuilder {

  protected StringBuilder beginSoapEnvelope() {
    return new StringBuilder(
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
  }

  protected StringBuilder beginSoapBody() {
    return new StringBuilder(
        "<soapenv:Body><GTIService xmlns=\"http://gtmotive.com/\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><GtRequestXml>");
  }

  protected StringBuilder endSoapBody() {
    return new StringBuilder("</GtRequestXml></GTIService></soapenv:Body>");
  }

  @Override
  protected StringBuilder endSoapEnvelope() {
    return new StringBuilder("</soapenv:Envelope>");
  }

  protected StringBuilder authenticationData(AuthenticationData authenData) {
    StringBuilder contents = new StringBuilder();
    contents.append("<autenticationData>");
    contents.append("<gsId value=\"").append(authenData.getGsId()).append(TAG_END);
    contents.append("<gsPwd value=\"").append(authenData.getGsPwd()).append(TAG_END);
    contents.append("<customerId value=\"").append(authenData.getCustomerId()).append(TAG_END);
    contents.append("<userId value=\"").append(authenData.getUserId()).append(TAG_END);
    contents.append("</autenticationData>");
    return contents;
  }
}
