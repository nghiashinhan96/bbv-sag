package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.ax.exception.ErrorInfo;
import com.sagag.services.common.enums.order.EshopOrderHistoryState;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.domain.sag.erp.OrderErrorMessage;
import com.sagag.services.service.exception.OrderException;
import com.sagag.services.service.exception.OrderException.OrderErrorCase;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;

import io.netty.channel.ConnectTimeoutException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.SocketTimeoutException;
import java.util.Objects;

@Component
@Slf4j
public class OrderingStepHandler {

  public OrderConfirmation handle(final UserInfo user, final AbstractOrderProcessor processor,
      final ExternalOrderRequest extOrderRequest, OrderHistory orderHistory) {
    if (user.isFinalUserRole()) {
      return new OrderConfirmation();
    }

    final String companyName = user.getCompanyName();
    OrderConfirmation axResult = new OrderConfirmation();
    OrderException orderException = null;
    try {
      axResult = processor.executeSendOrder(companyName, extOrderRequest);
    } catch (HttpServerErrorException ex) {
      if (ex.getRawStatusCode() == HttpStatus.GATEWAY_TIMEOUT.value()) {
        processor.updateOrderHistory(orderHistory, null, EshopOrderHistoryState.ERP_TIMEOUT_ERROR,
            user.getCustNrStr());
        orderException =
            new OrderException(companyName, extOrderRequest, OrderErrorCase.OE_STO_002, ex);
      } else {
        orderException =
            handleCommonException(user, processor, extOrderRequest, orderHistory, companyName, ex);
      }
    } catch (HttpClientErrorException ex) {
      processor.updateOrderHistory(orderHistory, null, EshopOrderHistoryState.ERP_ORDER_ERROR,
          user.getCustNrStr());
      if (ex.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
        orderException =
            handleCommonException(user, processor, extOrderRequest, orderHistory, companyName, ex);
      } else {
        if (SagJSONUtil.convertJsonToObject(ex.getResponseBodyAsString(), ErrorInfo.class)
            .hasData()) {
          orderException =
              new OrderException(companyName, extOrderRequest, OrderErrorCase.OE_STO_003, ex);
        } else {
          orderException =
              new OrderException(companyName, extOrderRequest, OrderErrorCase.OE_STO_004, ex);
        }
      }
    } catch (Exception ex) {
      if (isTimeoutException(ex)) {
        processor.updateOrderHistory(orderHistory, null, EshopOrderHistoryState.ERP_TIMEOUT_ERROR,
            user.getCustNrStr());
        orderException =
            new OrderException(companyName, extOrderRequest, OrderErrorCase.OE_STO_001, ex);
      } else {
        orderException =
            handleCommonException(user, processor, extOrderRequest, orderHistory, companyName, ex);
      }
    }
    if (!Objects.isNull(orderException)) {
      log.error("Create order has error:", orderException);
      axResult.setErrorMsg(OrderErrorMessage.builder().code(orderException.getCode())
          .key(orderException.getKey()).message(orderException.getMessage())
          .moreInfos(orderException.getMoreInfos()).build());
    }
    axResult.setOrderHistoryId(orderHistory.getId());
    return axResult;
  }

  private OrderException handleCommonException(final UserInfo user,
      final AbstractOrderProcessor processor, final ExternalOrderRequest extOrderRequest,
      OrderHistory orderHistory, final String companyName, Exception ex) {
    processor.updateOrderHistory(orderHistory, null, EshopOrderHistoryState.ERP_ORDER_ERROR,
        user.getCustNrStr());
    return new OrderException(companyName, extOrderRequest, ex);
  }

  private static boolean isTimeoutException(Exception ex) {
    return ex.getCause() instanceof ConnectTimeoutException
        || ex.getCause() instanceof SocketTimeoutException;
  }
}
