package com.sagag.services.service.order.processor.tm;

import com.sagag.services.article.api.SoapSendOrderExternalService;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.service.order.processor.AbstractPlaceOrderProcessor;
import com.sagag.services.stakis.erp.domain.TmSendOrderExternalRequest;
import com.sagag.services.stakis.erp.utils.StakisConstants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@CzProfile
public class TmPlaceOrderProcessor extends AbstractPlaceOrderProcessor {

  @Autowired
  private SoapSendOrderExternalService sendOrderExtService;

  @Override
  public OrderConfirmation executeSendOrder(String companyName, ExternalOrderRequest request)
      throws ServiceException {
    final TmSendOrderExternalRequest tmRequest = TmSendOrderExternalRequest.class.cast(request);
    final String username = tmRequest.getUsername();
    final String custId = tmRequest.getCustomerId();
    final String password = tmRequest.getSecurityToken();
    final String language = tmRequest.getLanguage();
    final AdditionalSearchCriteria additional = tmRequest.getAdditional();

    // #2669: Override sales origin of Stakis ERP
    if (!StringUtils.isBlank(request.getSalesUsername())) {
      request.setSalesOriginId(StakisConstants.CZ_SALES_ORIGIN_ID);
    }
    return sendOrderExtService.sendOrder(username, custId, password, language, request, additional);
  }

}
