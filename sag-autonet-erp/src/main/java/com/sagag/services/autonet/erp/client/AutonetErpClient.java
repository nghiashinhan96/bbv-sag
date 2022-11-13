package com.sagag.services.autonet.erp.client;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationRequestBodyType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationResponseBodyType;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.common.utils.XmlUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AutonetProfile
public class AutonetErpClient {

  private static final String LOG_INFO_REQUEST = "XML SoapAction = {} \nXMLRequest = \n{}";

  private static final String LOG_INFO_RESPONSE = "XML Response = \n{}";

  private static final String TIME_OUT_ERROR_LOG_MSG = "ERP-AUTONET-TIMEOUT {}\n{}";

  @Autowired
  @Qualifier("autonetErpWebServiceTemplate")
  private WebServiceTemplate wsTemplate;

  /**
   * Executes Autonet ERP WS to retrieve Prices and Availabilities of articles.
   *
   * @param request the request info
   * @param soapAction the SOAP action of request
   * @return GetErpInformationResponseBodyType the result from Autonet ERP
   */
  public GetErpInformationResponseBodyType getErpInformation(
      GetErpInformationRequestBodyType request, SoapActionCallback soapAction) {
    Assert.notNull(request, "The given erp information request must not be null");
    Assert.notNull(soapAction, "The given SOAP Action must not be empty");
    return exchange(request, soapAction);
  }

  @SuppressWarnings("unchecked")
  private <T> T exchange(Object request, SoapActionCallback soapAction) {
    final String requestBody = XmlUtils.marshalWithPrettyMode(request);
    log.info(LOG_INFO_REQUEST, soapAction, requestBody);

    try {
      final T response =
          ((JAXBElement<T>) (wsTemplate.marshalSendAndReceive(request, soapAction))).getValue();
      log.info(LOG_INFO_RESPONSE, XmlUtils.marshalWithPrettyMode(response));
      return response;
    } catch (WebServiceIOException ex) {
      final Throwable cause = ex.getCause();
      if (cause instanceof SocketTimeoutException) {
        log.error(TIME_OUT_ERROR_LOG_MSG, LocalDateTime.now(), requestBody);
      }
      throw ex;
    }
  }

}
