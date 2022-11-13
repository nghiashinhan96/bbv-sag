package com.sagag.services.dvse.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;

import com.sagag.services.dvse.api.HappyBonusValidateTokenService;
import com.sagag.services.dvse.dto.bonus.ValidateRecognitionResponse;

@Endpoint
public class HappyBonusValidateRecognitionEndpoint {

  private static final String NAMESPACE_URI = "http://sei.shopfacade.ws.ebl.sagag.com/";
  private static final String TOKEN_XPATH = "//*[local-name()='token']";

  @Autowired
  private HappyBonusValidateTokenService validateTokenService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "validateRecognition")
  @ResponsePayload
  public ValidateRecognitionResponse validateRecognition(
      @RequestPayload @XPathParam(TOKEN_XPATH) String token) {
    return validateTokenService.validateToken(token);
  }

}
