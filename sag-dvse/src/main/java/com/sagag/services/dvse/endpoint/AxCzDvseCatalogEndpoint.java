package com.sagag.services.dvse.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.sagag.services.dvse.api.AxCzCatalogService;
import com.sagag.services.dvse.profiles.AxCzDvseProfile;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationResponse;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrder;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderResponse;

@Endpoint
@AxCzDvseProfile
public class AxCzDvseCatalogEndpoint {

  private static final String NAMESPACE_URI = "http://topmotive.eu/TMConnect";

  @Autowired
  private AxCzCatalogService axCzcatalogService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetErpInformation")
  @ResponsePayload
  public GetErpInformationResponse getArticleInfo(
      @RequestPayload GetErpInformation request) {
    return axCzcatalogService.getArticleInfos(request);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendOrder")
  @ResponsePayload
  public SendOrderResponse addItemsToCart(@RequestPayload SendOrder request) {
    return axCzcatalogService.addItemsToCart(request);
  }
}
