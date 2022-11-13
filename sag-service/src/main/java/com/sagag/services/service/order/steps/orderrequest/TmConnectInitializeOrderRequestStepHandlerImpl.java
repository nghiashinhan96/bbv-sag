package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.request.order.OrderContextBuilder;
import com.sagag.services.stakis.erp.domain.TmSendOrderExternalRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CzProfile
public class TmConnectInitializeOrderRequestStepHandlerImpl
  extends AbstractOrderRequestStepHandlerV2Impl {

  @Override
  public ExternalOrderRequest handle(UserInfo user, SupportedAffiliate affiliate,
      String axOrderType, List<ShoppingCartItem> cartItems, OrderContextBuilder orderBuilder,
      ExecutionOrderType orderExecutionType) {
    final ExternalOrderRequest request =
        super.handle(user, affiliate, axOrderType, cartItems, orderBuilder, orderExecutionType);

    ignoreErpReturnedQuantityToGetFullRequestedQuantity(request, cartItems);

    final TmSendOrderExternalRequest tmRequest =
        SagBeanUtils.map(request, TmSendOrderExternalRequest.class);

    Optional.ofNullable(user.getExternalUserSession())
    .ifPresent(aUser -> {
      tmRequest.setUsername(aUser.getUser());
      tmRequest.setCustomerId(aUser.getCustomerId());
      tmRequest.setSecurityToken(aUser.getUid());
      tmRequest.setLanguage(user.getLanguage());
    });
    return tmRequest;
  }

  private void ignoreErpReturnedQuantityToGetFullRequestedQuantity(ExternalOrderRequest request,
      List<ShoppingCartItem> cartItems) {
    CollectionUtils.emptyIfNull(request.getBasketPositions()).forEach(basket -> {
      Optional<ShoppingCartItem> cartItemOpt =
          CollectionUtils
              .emptyIfNull(cartItems).stream().filter(cart -> StringUtils
                  .equalsIgnoreCase(cart.getArticle().getArtid(), basket.getArticleId()))
              .findFirst();
      cartItemOpt.ifPresent(cartItem -> basket.setQuantity(cartItem.getQuantity()));
    });
  }

}
