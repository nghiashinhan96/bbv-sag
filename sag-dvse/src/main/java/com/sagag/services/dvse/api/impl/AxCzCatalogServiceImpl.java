package com.sagag.services.dvse.api.impl;

import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.dvse.api.AxCzCatalogService;
import com.sagag.services.dvse.api.AxCzDvseArticleService;
import com.sagag.services.dvse.authenticator.mdm.AxCzConnectDvseUserAuthenticator;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.profiles.AxCzDvseProfile;
import com.sagag.services.dvse.wsdl.tmconnect.Credentials;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationResponse;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrder;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AxCzDvseProfile
@Transactional
public class AxCzCatalogServiceImpl implements AxCzCatalogService {

  @Autowired
  private AxCzConnectDvseUserAuthenticator authenticator;

  @Autowired
  private AxCzDvseArticleService axCzDvseArticleService;

  @Autowired
  private AxCzDvseCartServiceImpl axCzDvseCartServiceImpl;

  @Override
  public GetErpInformationResponse getArticleInfos(GetErpInformation request) {
    log.info("The [GetErpInformation] XML request: \n{} ",
        XmlUtils.marshalWithPrettyMode(request));

    if (request == null || request.getRequest() == null) {
      throw new IllegalArgumentException("The get article information request must not be null");
    }

    final Function<ConnectUser, GetErpInformationResponse> processor = user -> {
      return axCzDvseArticleService.getArticleInformation(user, request);
    };

    final Credentials credentials = request.getRequest().getCredentials();
    final String extSessionId = request.getRequest().getExternalSessionId();
    final GetErpInformationResponse result =
        authenticator.authenticateAndExecuteFunction(credentials, extSessionId, processor);
    log.info("The [GetErpInformationResponse] XML response: \n{}",
        XmlUtils.marshalWithPrettyMode(result));
    return result;
  }

  @Override
  public SendOrderResponse addItemsToCart(SendOrder request) {
    log.info("The [SendOrder] XML request: \n{} ", XmlUtils.marshalWithPrettyMode(request));
    if (request == null || request.getRequest() == null) {
      throw new IllegalArgumentException("The send order request must not be null");
    }

    final Function<ConnectUser, SendOrderResponse> processor = user -> {
      return axCzDvseCartServiceImpl.addItemsToCart(user, request);
    };

    final Credentials credentials = request.getRequest().getCredentials();
    final String extSessionId = request.getRequest().getExternalSessionId();;
    final SendOrderResponse result =
        authenticator.authenticateAndExecuteFunction(credentials, extSessionId, processor);
    log.info("The [SendOrderResponse] XML response: \n{}", XmlUtils.marshalWithPrettyMode(result));
    return result;
  }

}
