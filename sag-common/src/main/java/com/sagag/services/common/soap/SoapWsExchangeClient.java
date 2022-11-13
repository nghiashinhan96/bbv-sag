package com.sagag.services.common.soap;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.XmlUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

@Component
@Slf4j
public class SoapWsExchangeClient {

  private static final String LOG_INFO_REQUEST = "XML SoapAction = {} \nXML Request = \n{}";

  private static final String LOG_INFO_RESPONSE = "XML Response = \n{}";

  public <T, R> R exchange(final WebServiceTemplate wsTemplate,
      final Jaxb2Marshaller jaxb2Marshaller, final String wsServiceUri,
      final JAXBElement<T> request, final SoapActionCallback soapAction) {
    return this.exchange(wsTemplate, jaxb2Marshaller.getJaxbContext(), wsServiceUri, request,
        soapAction);
  }

  public <T, R> R exchange(final WebServiceTemplate wsTemplate,
      final JAXBContext jaxbContext, final T request,
      final SoapActionCallback soapAction) {
    return exchange(wsTemplate, jaxbContext, StringUtils.EMPTY, request, soapAction);
  }

  @LogExecutionTime(value = "Perf:SoapWsExchangeClient -> exchange -> Execute SOAP WS in {} ms",
      infoMode = true)
  @SuppressWarnings("unchecked")
  public <T, R> R exchange(final WebServiceTemplate wsTemplate,
      final JAXBContext jaxbContext,
      final String wsServiceUri, final T request,
      final SoapActionCallback soapAction) {
    log.info(LOG_INFO_REQUEST, soapAction, XmlUtils.marshalWithPrettyMode(jaxbContext, request));

    final JAXBElement<R> response;
    if (StringUtils.isBlank(wsServiceUri)) {
      response = (JAXBElement<R>) wsTemplate.marshalSendAndReceive(request, soapAction);
    } else {
      response = (JAXBElement<R>) wsTemplate.marshalSendAndReceive(wsServiceUri,
          request, soapAction);
    }

    if (response == null) {
      return null;
    }
    log.info(LOG_INFO_RESPONSE, XmlUtils.marshalWithPrettyMode(jaxbContext, response));
    return response.getValue();
  }

}
