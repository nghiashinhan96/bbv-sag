package com.sagag.services.service.order.processor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.enums.AxOrderType;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;

import java.util.Objects;

/**
 * This class provides implementation to place order to ax system.
 *
 */
public abstract class AbstractPlaceOrderProcessor extends AbstractOrderProcessor {

  @Override
  public String prepareOrderDetailUrl(OrderConfirmation orderConfirm, String custNr) {
    final Link selfLink = MapUtils.emptyIfNull(orderConfirm.getLinks()).get(Link.REL_SELF);
    if (!StringUtils.isBlank(orderConfirm.getOrderNr()) && !Objects.isNull(selfLink)) {
      return new StringBuilder().append(selfLink.getHref()).append(SagConstants.SLASH)
          .append(custNr).append(SagConstants.SLASH).append(orderConfirm.getOrderNr())
          .append(SagConstants.SLASH).append("positions").toString();
    }
    return StringUtils.EMPTY;
  }

  @Override
  public OrderType orderHistoryType() {
    return OrderType.ORDER;
  }

  @Override
  public boolean allowSendOrderConfirmationMail(UserInfo user) {
    return true;
  }

  @Override
  public String axOrderType(UserInfo user, OrderConditionContextBody orderCondition) {
    SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    if (affiliate.isAtAffiliate()) {
      // #4969 the changes is applied for CH only
      return StringUtils.EMPTY;
    }
    if (isAbsEnabled(user) && orderCondition.getSendMethodType() == SendMethodType.PICKUP) {
      return AxOrderType.ABS.name();
    }
    return StringUtils.EMPTY;
  }

  private boolean isAbsEnabled(UserInfo user) {
    if (user.isSaleOnBehalf()) {
      return user.getSettings().isSalesAbsEnabled();
    }
    return user.getSettings().isCustomerAbsEnabled();
  }

  @Override
  public OrderRequestType orderRequestType() {
    return OrderRequestType.PLACE_ORDER;
  }

  @Override
  public final ExecutionOrderType orderExecutionType() {
    return ExecutionOrderType.ORDER;
  }

}
