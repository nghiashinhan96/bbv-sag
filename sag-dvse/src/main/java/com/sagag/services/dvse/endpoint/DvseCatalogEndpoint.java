package com.sagag.services.dvse.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.sagag.services.dvse.api.CatalogService;
import com.sagag.services.dvse.profiles.DefaultDvseProfile;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformation;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformationResponse;
import com.sagag.services.dvse.wsdl.dvse.SendOrder;
import com.sagag.services.dvse.wsdl.dvse.SendOrderResponse;

/**
 * <p>
 * The end point to process request from client to web services.
 * </p>
 *
 *
 */
@Endpoint
@DefaultDvseProfile
public class DvseCatalogEndpoint {

  private static final String NAMESPACE_URI = "DVSE";

  @Autowired
  private CatalogService catalogService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetArticleInformation")
  @ResponsePayload
  public GetArticleInformationResponse getArticleInfo(@RequestPayload GetArticleInformation request) {
    return catalogService.getArticleInfos(request);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendOrder")
  @ResponsePayload
  public SendOrderResponse addItemsToCart(@RequestPayload SendOrder request) {
    return catalogService.addItemsToCart(request);
  }
}
