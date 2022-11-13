package com.sagag.services.dvse.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.TransportInputStream;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class DualProtocolSoapMessageFactory implements WebServiceMessageFactory, InitializingBean {

  private Map<String, SaajSoapMessageFactory> saajSoapMessageFactories;

  @NonNull
  private String[] protocols;

  @Override
  public void afterPropertiesSet() {
    Assert.notEmpty(protocols, "protocols must not be empty");
    saajSoapMessageFactories = new HashMap<>();
    for (String protocol : protocols) {
      try {
        saajSoapMessageFactories.putIfAbsent(protocol,
            new SaajSoapMessageFactory(MessageFactory.newInstance(protocol)));
      } catch (SOAPException ex) {
        log.error("Creating SOAP Message Factory has error", ex);
      }
    }
  }

  @Override
  public WebServiceMessage createWebServiceMessage() {
    return saajSoapMessageFactories.get(SOAPConstants.SOAP_1_1_PROTOCOL).createWebServiceMessage();
  }

  @Override
  public SaajSoapMessage createWebServiceMessage(InputStream inputStream) throws IOException {
    return saajSoapMessageFactories.get(getSoapProtocol(inputStream))
        .createWebServiceMessage(inputStream);
  }

  private String getSoapProtocol(InputStream inputStream) throws IOException {
    MimeHeaders mimeHeaders = parseMimeHeaders(inputStream);
    if (mimeHeaders.getHeader(HttpHeaders.CONTENT_TYPE)[0].contains(MimeTypeUtils.TEXT_XML_VALUE)) {
      return SOAPConstants.SOAP_1_1_PROTOCOL;
    }
    return SOAPConstants.SOAP_1_2_PROTOCOL;
  }

  private MimeHeaders parseMimeHeaders(InputStream inputStream) throws IOException {
    MimeHeaders mimeHeaders = new MimeHeaders();
    if (inputStream instanceof TransportInputStream) {
      TransportInputStream transportInputStream = (TransportInputStream) inputStream;
      for (Iterator<String> headerNames = transportInputStream.getHeaderNames();
            headerNames.hasNext();) {
        String headerName = headerNames.next();
        for (Iterator<String> headerValues = transportInputStream.getHeaders(headerName);
              headerValues.hasNext();) {
          String headerValue = headerValues.next();
          StringTokenizer tokenizer = new StringTokenizer(headerValue, ",");
          while (tokenizer.hasMoreTokens()) {
            mimeHeaders.addHeader(headerName, tokenizer.nextToken().trim());
          }
        }
      }
    }
    return mimeHeaders;
  }

}
