package com.sagag.services.service.utils.order;

import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.article.api.request.VehicleRequest;
import com.sagag.services.ax.enums.AxOrderType;
import com.sagag.services.ax.request.AxOrderRequest;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxConstants;
import com.sagag.services.common.enums.OrderSource;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
@Slf4j
public class OrderRequestBuilderV2 {

  private static final int DISCOUNT_ITEM_QUANTITY = -1;

  private static final String SOURCING_TYPE_KSO = "KSO";

  private SupportedAffiliate affiliate;

  private String custNr;

  private CreateOrderRequestBodyV2 request;

  private OrderConditionContextBody orderCondition;

  private List<ShoppingCartItem> cartItems;

  private boolean salesOnBehalf;

  private String salesUsername;

  private String orderType;

  private String defaultBranchId;

  private CouponUseLog couponUseLog;

  private String orderFrom;

  private String userName;

  private String email;

  public ExternalOrderRequest build() {
    Assert.notNull(affiliate, "The given affiliate must not be null");
    Assert.hasText(custNr, "The given customer number must not be null");
    Assert.notEmpty(cartItems, "The given client order request must not be null");
    Assert.notNull(request, "The given shopping cart must not be null");
    validateOrderCondition();
    
    final List<BasketPositionRequest> baskets =
        toBasketPositionRequest(cartItems, orderType, couponUseLog, this.affiliate);
    final AxOrderRequest axOrderRequest = new AxOrderRequest(custNr, baskets);

    axOrderRequest.setUsername(userName);
    axOrderRequest.setEmail(email);
    axOrderRequest.setCustomerRefText(getCustomerRefText());
    axOrderRequest.setMessage(getMessageToBranch());
    axOrderRequest.setPersonalNumber(request.getPersonalNumber());

    axOrderRequest.setPickupBranchId(orderCondition.getPickupBranchIdOrDefault(defaultBranchId));

    axOrderRequest.setBranchId(orderCondition.getLocationBranchId());

    final SendMethodType sendMethod = orderCondition.getSendMethodType();
    axOrderRequest.setSendMethodCode(sendMethod.name());
    axOrderRequest.setCourierServiceId(orderCondition.getCourierServiceId());

    final PaymentMethodType paymentMethod = orderCondition.getPaymentMethodType();
    axOrderRequest.setPaymentMethod(getPaymentMethod(sendMethod, paymentMethod).name());

    axOrderRequest.setDeliveryAddressId(orderCondition.getDeliveryAddressId());
    axOrderRequest.setInvoiceAddressId(orderCondition.getBillingAddressId());
    axOrderRequest.setPartialDelivery(orderCondition.isPartialDelivery());
    axOrderRequest.setSingleInvoice(orderCondition.isSingleInvoice());
    if (StringUtils.isNotBlank(orderFrom)) {
      axOrderRequest.setSalesOriginId(OrderSource.fromText(orderFrom).getSource());
    } else if (salesOnBehalf) {
      axOrderRequest.setSalesOriginId(AxConstants.ESHOP_SALES_ORIGIN_ID);
      axOrderRequest.setSalesUsername(salesUsername);
    } else {
      axOrderRequest.setSalesOriginId(StringUtils.trim(affiliate.getSalesOriginId()));
    }

    axOrderRequest.setOrderType(handleOrderType(orderType));

    return axOrderRequest;
  }

  private String getCustomerRefText() {
    if (isSbAffiliate()) {
      return orderCondition.getReferenceTextByLocation();
    }
    return request.getCustomerRefText();
  }

  private String getMessageToBranch() {
    if (isSbAffiliate()) {
      return orderCondition.getMessageToBranch();
    }
    return request.getMessage();
  }

  private String handleOrderType(String orderType) {
    if (StringUtils.isNotEmpty(orderType) && AxOrderType.valueOf(orderType).isKsoMix()) {
      return AxOrderType.STD.name();
    }
    return orderType;
  }

  private PaymentMethodType getPaymentMethod(final SendMethodType erpSendMethod,
      final PaymentMethodType paymentMethod) {
    log.debug(
        "Processing payment method before create order request with sale on behalf mode = {}"
            + ", erp send method = {} and payment method = {}",
        salesOnBehalf, erpSendMethod, paymentMethod);
    if (salesOnBehalf) {
      return paymentMethod;
    }

    // #3418: If the customer orders with Barzahlung (CASH) then always DIRECT INVOICE is sent to AX
    return isSpecialCaseForCash(paymentMethod) ? PaymentMethodType.DIRECT_INVOICE : paymentMethod;
  }

  private boolean isSpecialCaseForCash(final PaymentMethodType paymentMethod) {
    return paymentMethod.isCash() && !isSbAffiliate();
  }

  private static List<BasketPositionRequest> toBasketPositionRequest(
      final List<ShoppingCartItem> cartItems, String orderType,
      CouponUseLog couponUseLog, SupportedAffiliate affiliate) {

    if (CollectionUtils.isEmpty(cartItems)) {
      return Collections.emptyList();
    }

    // Filter all articles with vehicle is the key, remove the all articles is non-reference
    final Map<VehicleDto, List<ShoppingCartItem>> articlesByVehicle =
        cartItems.stream().filter(item -> !item.isNonReference())
            .collect(Collectors.groupingBy(ShoppingCartItem::getVehicle));

    final List<BasketPositionRequest> basketPositionRequests = new ArrayList<>();
    for (Entry<VehicleDto, List<ShoppingCartItem>> entry : articlesByVehicle.entrySet()) {
      final VehicleDto vehicle = entry.getKey();
      final List<ShoppingCartItem> cartItemsByVeh = entry.getValue();
      if (CollectionUtils.isEmpty(cartItemsByVeh)) {
        continue;
      }

      final Optional<VehicleRequest> vehicleRequestOpt =
          VehicleRequest.createErpVehicleRequest(vehicle);
      final List<ArticleRequest> articleRequests =
          cartItemsByVeh.stream().map(articleRequestConverter(orderType, affiliate))
              .flatMap(Collection::stream).collect(Collectors.toList());
      basketPositionRequests.add(new BasketPositionRequest(articleRequests, vehicleRequestOpt));
    }
    if (!Objects.isNull(couponUseLog) && couponUseLog.isValidCoupon()) {
      basketPositionRequests.add(getDiscountItem(couponUseLog, affiliate));
    }
    return basketPositionRequests;
  }


  private static BasketPositionRequest getDiscountItem(CouponUseLog couponUseLog,
      SupportedAffiliate affiliate) {
    ArticleRequest articleRequest =
        AxArticleUtils.createErpArticleRequest(String.valueOf(couponUseLog.getDiscountArticleId()),
            DISCOUNT_ITEM_QUANTITY, StringUtils.EMPTY, affiliate);

    return new BasketPositionRequest(Arrays.asList(articleRequest), Optional.empty());
  }

  private static Function<ShoppingCartItem, List<ArticleRequest>> articleRequestConverter(
      String orderType, SupportedAffiliate affiliate) {
    return cartItem -> {
      final ArticleDocDto article = cartItem.getArticleItem();

      final String artId = article.getIdSagsys();

      final String additionalTextDoc =
          StringUtils.defaultString(cartItem.getReference(), StringUtils.EMPTY);

      List<ArticleRequest> articleRequests = new ArrayList<>();

      boolean isOrderTypeForVEN =
          StringUtils.isNoneEmpty(orderType) && AxOrderType.valueOf(orderType).isKsoAut();
      if (isOrderTypeForVEN) {
        ListUtils.emptyIfNull(article.getAvailabilities()).stream()
            .filter(Availability::isVenExternalSource).findFirst().ifPresent(avai -> {
              ArticleRequest venRequest = AxArticleUtils.createErpArticleRequest(artId,
                  avai.getQuantity(), additionalTextDoc, SOURCING_TYPE_KSO,
                  String.valueOf(avai.getVendorId()), avai.getArrivalTimeAtBranch(), affiliate);
              OrderDisplayPriceHelper.updateSelectedPrice(venRequest, article);
              articleRequests.add(venRequest);
            });
      }

      List<Availability> nonVenAvais = ListUtils.emptyIfNull(article.getAvailabilities()).stream()
          .filter(Availability::isNonVenExternal).collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(nonVenAvais)) {
        Integer remainQuatity = findRemainQuantity(cartItem, nonVenAvais);
        ArticleRequest nonvenRequest =
            AxArticleUtils.createErpArticleRequest(artId, remainQuatity, additionalTextDoc, affiliate);
        OrderDisplayPriceHelper.updateSelectedPrice(nonvenRequest, article);
        articleRequests.add(nonvenRequest);
      }

      return articleRequests;
    };
  }
  
  private void validateOrderCondition() {
    if (isSbAffiliate()) {
      Assert.notEmpty(request.getOrderConditionByLocation(),
          "The given order condition must not be null");
    }
    Assert.notNull(orderCondition, "The given order condition must not be null");
  }
  
  private boolean isSbAffiliate() {
    return StringUtils.equalsIgnoreCase(affiliate.getShortName(),
        com.sagag.services.common.enums.SupportedAffiliate.WINT_SB.getAffiliate());
  }

  private static Integer findRemainQuantity(ShoppingCartItem cartItem,
      List<Availability> nonVenAvais) {
    Integer remainQuatity =
        nonVenAvais.stream().collect(Collectors.summingInt(Availability::getDefaultQuantity));
    if (BooleanUtils.isTrue(cartItem.isVin()) && remainQuatity == 0) {
      remainQuatity = cartItem.getQuantity();
    }
    return remainQuatity;
  }

}
