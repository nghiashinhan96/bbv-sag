package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.request.order.OrderContextBuilder;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@SbProfile
public class SbConnectInitializeOrderRequestStepHandlerImpl
    extends AbstractOrderRequestStepHandlerV2Impl {

  private static final int CUSTOMER_REFERENCE_TEXT_MAX_LENGTH = 20;
  private static final int MAKE_MODEL_TYPE_MAX_LENGTH = 30;

  @Override
  public ExternalOrderRequest handle(UserInfo user, SupportedAffiliate affiliate,
      String axOrderType, List<ShoppingCartItem> cartItems, OrderContextBuilder orderBuilder,
      ExecutionOrderType orderExecutionType) {
    final ExternalOrderRequest request =
        super.handle(user, affiliate, axOrderType, cartItems, orderBuilder, orderExecutionType);

    request.setUsername(StringUtils.defaultString(user.getUsername()));
    request.setEmail(StringUtils.defaultString(user.getEmail()));

    ListUtils.emptyIfNull(request.getBasketPositions()).forEach(basketPos -> {
      basketPos.setBrand(trimdownStringToSize(basketPos.getBrand(), MAKE_MODEL_TYPE_MAX_LENGTH));
      basketPos.setModel(trimdownStringToSize(basketPos.getModel(), MAKE_MODEL_TYPE_MAX_LENGTH));
      basketPos.setType(trimdownStringToSize(basketPos.getType(), MAKE_MODEL_TYPE_MAX_LENGTH));
    });

    Assert.isTrue(StringUtils.length(request.getCustomerRefText()) <= CUSTOMER_REFERENCE_TEXT_MAX_LENGTH,
        " Customer Reference text could limited with " + CUSTOMER_REFERENCE_TEXT_MAX_LENGTH
            + "characters only");

    resetPartialDelivery(request);
    return request;
  }

  private String trimdownStringToSize(String string, int size) {
    return StringUtils.left(string, size);
  }

  private void resetPartialDelivery(final ExternalOrderRequest request) {
    request.setPartialDelivery(false);
  }

}
