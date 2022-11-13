package com.sagag.services.stakis.erp.client;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.soap.SoapWsExchangeClient;
import com.sagag.services.stakis.erp.builder.TmArticleErpInfoRequestBuilder;
import com.sagag.services.stakis.erp.builder.TmSendOrderRequestBuilder;
import com.sagag.services.stakis.erp.domain.TmArticlePriceAndAvailabilityRequest;
import com.sagag.services.stakis.erp.domain.TmSendOrderExternalRequest;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationRequestBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SendOrderRequestBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SendOrderResponseBody;

@Component
@CzProfile
public class StakisErpTmConnectClient {

  @Autowired
  @Qualifier("stakisErpTmConnectWebServiceTemplate")
  private WebServiceTemplate wsTemplate;

  @Autowired
  private Jaxb2Marshaller stakisErpTmConnectMarshaller;

  @Autowired
  private TmArticleErpInfoRequestBuilder tmArticleErpInfoRequestBuilder;

  @Autowired
  private TmSendOrderRequestBuilder tmSendOrderRequestBuilder;

  @Autowired
  private SoapWsExchangeClient exchangeClient;

  public GetErpInformationResponseBody getERPInformation(final TmUserCredentials credentials,
      final TmArticlePriceAndAvailabilityRequest request) {
    Assert.notNull(credentials, "The given credentails must not be null");
    Assert.notNull(request, "The given article request must not be null");

    final GetErpInformationRequestBody requestElement =
        tmArticleErpInfoRequestBuilder.buildRequest(credentials, request);

    return exchangeClient.exchange(wsTemplate, stakisErpTmConnectMarshaller.getJaxbContext(), requestElement,
        TmConnectSoapAction.SOAP_ACTION_GET_ERP_INFORMATION);
  }

  public SendOrderResponseBody sendOrder(final TmUserCredentials credentials,
      final TmSendOrderExternalRequest request) {
    Assert.notNull(credentials, "The given credentails must not be null");
    Assert.notNull(request, "The given external order request must not be null");

    final JAXBElement<SendOrderRequestBody> requestElement =
        tmSendOrderRequestBuilder.buildRequest(credentials, request);

    return exchangeClient.exchange(wsTemplate, stakisErpTmConnectMarshaller.getJaxbContext(),
        requestElement, TmConnectSoapAction.SOAP_ACTION_SEND_ORDER);
  }

}
