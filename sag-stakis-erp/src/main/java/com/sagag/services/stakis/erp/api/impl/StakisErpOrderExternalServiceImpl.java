package com.sagag.services.stakis.erp.api.impl;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.SoapSendOrderExternalService;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPosition;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.stakis.erp.client.StakisErpCisClient;
import com.sagag.services.stakis.erp.client.StakisErpTmConnectClient;
import com.sagag.services.stakis.erp.domain.TmSendOrderExternalRequest;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfVoucher;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfVoucherItem;
import com.sagag.services.stakis.erp.wsdl.cis.OutVoucherDetails;
import com.sagag.services.stakis.erp.wsdl.cis.OutVouchers;
import com.sagag.services.stakis.erp.wsdl.cis.Voucher;
import com.sagag.services.stakis.erp.wsdl.cis.VoucherItem;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfOrder;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfOrderId;
import com.sagag.services.stakis.erp.wsdl.tmconnect.OrderCollection;
import com.sagag.services.stakis.erp.wsdl.tmconnect.OrderId;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SendOrderReply;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SendOrderResponseBody;

@Service
@CzProfile
public class StakisErpOrderExternalServiceImpl
  extends StakisProcessor implements SoapSendOrderExternalService {

	private static final int SECOND_ELEMENT_IN_ORDER = 1;

	@Autowired
	private StakisErpTmConnectClient tmConnectClient;

	@Autowired
	private StakisErpCisClient cisClient;

  @Override
  public OrderConfirmation sendOrder(String username, String customerId, String password,
      String language, ExternalOrderRequest request, AdditionalSearchCriteria additional)
          throws ServiceException {
    final String salesOriginId = request.getSalesOriginId();
    final String salesUsername = request.getSalesUsername();
    final TmUserCredentials credentials = buildTmUserCredentialsWithSalesAdvisor(username, password,
        customerId, language, salesOriginId, salesUsername);
    final SendOrderResponseBody response =
        tmConnectClient.sendOrder(credentials, TmSendOrderExternalRequest.class.cast(request));
    final Optional<SendOrderReply> sendOrderReplyOpt = getValueOpt(response.getSendOrderResult());
    if (sendOrderReplyOpt
          .filter(sendOrderReply -> sendOrderReply.getStatusId() != 0).isPresent()) {
      throw new ServiceException(sendOrderReplyOpt.get().getErrorMessage().getValue());
    }
    return sendOrderReplyOpt.map(orderResponseConverter()).orElse(new OrderConfirmation());
  }

  @Override
  public Optional<ExternalOrderHistory> getExternalOrderHistoryOfCustomer(String companyName,
      String customerNr, ExternalOrderHistoryRequest request, Integer page) {
    OutVouchers voucher = cisClient.getOrderHistoryFromVoucher(customerNr, StringUtils.EMPTY,
        request, page, Locale.ENGLISH.getLanguage());
    Optional<ArrayOfVoucher> voucherOpt = getValueOpt(voucher.getVouchers());
    List<ExternalOrderDetail> orderDetails = new ArrayList<>();
    if (voucherOpt.isPresent()) {
      orderDetails = CollectionUtils.emptyIfNull(voucherOpt.get().getVoucher()).stream()
          .map(externalOrderHistoryConverter()).collect(Collectors.toList());
    }
    return Optional.of(ExternalOrderHistory.builder().orders(orderDetails).build());
  }

  @Override
  public Optional<ExternalOrderPositions> getExternalOrderPosistion(String orderNr, String companyName,
      String customerNr) {
    OutVoucherDetails voucherDetail = cisClient.getOrderHistoryDetailFromVoucher(customerNr,
        orderNr, Locale.ENGLISH.getLanguage());
    Optional<ArrayOfVoucherItem> orderPosOpt = getValueOpt(voucherDetail.getVoucherItems());
    List<ExternalOrderPosition> orderPositions = new ArrayList<>();
    if (orderPosOpt.isPresent()) {
      orderPositions = CollectionUtils.emptyIfNull(orderPosOpt.get().getVoucherItem()).stream()
          .map(externalOrderPositionConverter()).collect(Collectors.toList());
    }

    return Optional.of(ExternalOrderPositions.builder().positions(orderPositions).build());
  }

  private Function<Voucher, ExternalOrderDetail> externalOrderHistoryConverter() {
    return voucher -> {
      ExternalOrderDetail detail = new ExternalOrderDetail();
      Optional<XMLGregorianCalendar> date = Optional.of(voucher.getDate());
      if (date.isPresent()) {
        detail.setDate(DateUtils.getUTCDateStrWithTimeZone(date.get().toGregorianCalendar().getTime()));
      }
      detail.setNr(getValueOpt(voucher.getId()).orElse(StringUtils.EMPTY));
      detail.setSendMethodDesc(getValueOpt(voucher.getDeliveryType()).orElse(StringUtils.EMPTY));
      return detail;
		};
	}

  private Function<VoucherItem, ExternalOrderPosition> externalOrderPositionConverter() {
    return vItem -> {
      ExternalOrderPosition position = new ExternalOrderPosition();
      position.setArticleId(getValueOpt(vItem.getWholesalerArticleNumber()).orElse(StringUtils.EMPTY));
      position.setQuantity(Optional.of(vItem.getQuantity()).map(BigDecimal::intValue).orElse(null));
      return position;
    };
  }

  private static Function<SendOrderReply, OrderConfirmation> orderResponseConverter() {
    return bodyRespose -> {
      final OrderConfirmation orderConfirmation = new OrderConfirmation();
      final List<OrderId> orderIds = orderIdsExtractor().apply(bodyRespose);
      if (CollectionUtils.size(orderIds) > 1) {
        final OrderId placeOrderId9Digit = orderIds.get(SECOND_ELEMENT_IN_ORDER);
        orderConfirmation.setOrderNr(
                getValueOpt(placeOrderId9Digit.getValue()).orElse(StringUtils.EMPTY));
        orderConfirmation.setFrontEndBasketNr(
            getValueOpt(placeOrderId9Digit.getValue()).map(NumberUtils::toLong)
            .orElse(NumberUtils.LONG_ZERO));
      } else {
        CollectionUtils.emptyIfNull(orderIds).stream().findFirst()
        .ifPresent(orderId -> orderConfirmation.setOrderNr(
            getValueOpt(orderId.getValue()).orElse(StringUtils.EMPTY)));
      }
      return orderConfirmation;
    };
  }

  private static Function<SendOrderReply, List<OrderId>> orderIdsExtractor() {
    return responseBody -> {

      final Optional<OrderCollection> orderCollectionOpt =
          getValueOpt(responseBody.getOrderCollection());
      if (!orderCollectionOpt.isPresent()) {
        return Collections.emptyList();
      }

      final Optional<ArrayOfOrder> arrayOfOrderOpt =
          getValueOpt(orderCollectionOpt.get().getOrders());
      if (!arrayOfOrderOpt.isPresent()) {
        return Collections.emptyList();
      }

      final List<ArrayOfOrderId> arrayOfOrderIds = arrayOfOrderOpt.get().getOrder()
          .stream().map(order -> order.getOrderIds().getValue()).collect(Collectors.toList());

      return arrayOfOrderIds.stream().flatMap(orderIds -> orderIds.getOrderId().stream())
          .collect(Collectors.toList());
    };
  }

}
