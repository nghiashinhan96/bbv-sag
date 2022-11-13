package com.sagag.services.dvse.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.dvse.api.UnicatCatalogService;
import com.sagag.services.dvse.wsdl.unicat.GetArticleInformationResponse;
import com.sagag.services.dvse.wsdl.unicat.SendOrderResponse;
import com.sagag.services.dvse.wsdl.unicat.SendOrder;
import com.sagag.services.dvse.wsdl.unicat.GetArticleInformations;

@Endpoint
@CzProfile
public class UnicatCatalogEndpoint {

  private static final String NAMESPACE_URI = "UNICAT";

  @Autowired
  private UnicatCatalogService unicatCatalogService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetArticleInformations")
  @ResponsePayload
  public GetArticleInformationResponse getArticleInfo(@RequestPayload GetArticleInformations request) {
    return unicatCatalogService.getArticleInfos(request);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendOrder")
  @ResponsePayload
  public SendOrderResponse addItemsToCart(@RequestPayload SendOrder request) {
    return unicatCatalogService.addItemsToCart(request);
  }

}
