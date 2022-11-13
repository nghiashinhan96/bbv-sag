package com.sagag.services.dvse.api.impl;

import java.util.function.Function;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.dvse.api.CatalogService;
import com.sagag.services.dvse.api.DvseArticleService;
import com.sagag.services.dvse.api.DvseCartService;
import com.sagag.services.dvse.authenticator.mdm.ConnectDvseUserAuthenticator;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.dto.dvse.OrderedItemDto;
import com.sagag.services.dvse.profiles.DefaultDvseProfile;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformation;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformationResponse;
import com.sagag.services.dvse.wsdl.dvse.GetBackItems;
import com.sagag.services.dvse.wsdl.dvse.GetBackOrder;
import com.sagag.services.dvse.wsdl.dvse.SendOrder;
import com.sagag.services.dvse.wsdl.dvse.SendOrderResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * The service to wrap up the all specific logic and return to end point.
 */
@Service
@Slf4j
@DefaultDvseProfile
@Transactional
public class CatalogServiceImpl implements CatalogService {

  @Autowired
  private ConnectDvseUserAuthenticator authenticator;

  @Autowired
  private DvseArticleService dvseArticleService;

  @Autowired
  private DvseCartService dvseCartService;

  @Override
  public GetArticleInformationResponse getArticleInfos(final GetArticleInformation request) {
    log.info("The [GetArticleInformationResponse] XML request: \n{} ",
        XmlUtils.marshalWithPrettyMode(request));
    final Function<ConnectUser, GetArticleInformationResponse> processor = user -> {
      final GetBackItems backItems = dvseArticleService.getArticleInformation(user,
          request.getItems());
        final GetArticleInformationResponse response = new GetArticleInformationResponse();
        response.setGetArticleInformationResult(backItems);
        return response;
    };
    final String extSessionId = request.getUser().getExternalSessionId();
    final GetArticleInformationResponse result =
        authenticator.authenticateAndExecuteFunction(request.getUser(), extSessionId,  processor);
    log.info("The [GetArticleInformationResponse] XML response: \n{}",
        XmlUtils.marshalWithPrettyMode(result));
    return result;
  }

  @Override
  public SendOrderResponse addItemsToCart(final SendOrder request) {
    log.info("The [SendOrderResponse] XML request: \n{} ", XmlUtils.marshalWithPrettyMode(request));

    final Function<ConnectUser, SendOrderResponse> processor = user -> {
      final OrderedItemDto addedItemsDto = dvseCartService.addItemsToCart(user,
        request.getOrder(), request.getItems());

      final GetBackOrder backOrder = new GetBackOrder();
      backOrder.setItem(addedItemsDto.getOrder());
      backOrder.setOrderedItems(addedItemsDto.getAddedItems());

      final SendOrderResponse response = new SendOrderResponse();
      response.setSendOrderResult(backOrder);

      return response;
    };
    final String extSessionId = request.getUser().getExternalSessionId();
    final SendOrderResponse result =
        authenticator.authenticateAndExecuteFunction(request.getUser(), extSessionId, processor);

    log.info("The [SendOrderResponse] XML response: \n{}", XmlUtils.marshalWithPrettyMode(result));
    return result;
  }

}
