package com.sagag.services.service.mail.orderconfirmation;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.enums.DeliveryMethodType;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.request.order.OrderConditionContextBody;
import com.sagag.services.service.request.order.OrderContextBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.Map;
import java.util.TimeZone;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmationCriteria {

  private Long userId;

  private String langiso;

  private String firstName;

  private String lastName;

  private String email;

  private String customerNr;

  private String collectionShortName;

  private String affiliateInUrl;

  private String affiliateEmail;

  private ExternalOrderRequest orderRequest;

  private ShoppingCart shoppingCart;

  private Map<String, String> additionalTextDocMap;

  private String orderNr;

  private SendMethodType sendMethodType;

  private PaymentMethodType paymentMethodType;

  private DeliveryMethodType deliveryMethodType;

  private Address deliveryAddress;

  private Address billingAddress;

  private TimeZone timezone;

  private boolean isShowAvailability;

  private boolean isFinalUser;

  private boolean isShowPriceType;

  private OrganisationDto finalCustomer;

  private boolean isShowFinalCustomerNetPrice;

  private String customerCurrency;

  public static OrderConfirmationCriteria of(final UserInfo user,
      final OrderContextBuilder orderBuilder,
      final ExternalOrderRequest orderRequest,
      final ShoppingCart shoppingCart,
      final OrderConfirmation orderConfirmRes,
      final Address deliveryAddr,
      final Address billingAddr) {

    // Send mail function
    final DeliveryMethodType deliveryMethodType =
        orderRequest.getPartialDelivery() ? DeliveryMethodType.URGENT : DeliveryMethodType.NORMAL;
    final OrderConditionContextBody orderCondition = orderBuilder.getSelectedOrderCondition();
    final String affiliateEmail =
        user.isFinalUserRole() ? user.getSettings().getEhDefaultEmail() : user.getSettings().getAffiliateEmail();
    return OrderConfirmationCriteria.builder()
        .userId(user.getId()).langiso(user.getLanguage())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .customerNr(user.getCustNrStr())
        .affiliateEmail(affiliateEmail)
        .affiliateInUrl(user.getAffiliateShortName())
        .orderRequest(orderRequest)
        .shoppingCart(shoppingCart)
        .additionalTextDocMap(orderBuilder.getBody().getAdditionalTextDocs())
        .timezone(TimeZone.getTimeZone(ZoneId.of(orderBuilder.getBody().getTimezone())))
        .orderNr(orderConfirmRes.getOrderNr())
        .sendMethodType(orderCondition.getSendMethodType())
        .paymentMethodType(orderCondition.getPaymentMethodType())
        .deliveryMethodType(deliveryMethodType)
        .deliveryAddress(deliveryAddr)
        .billingAddress(billingAddr)
        .isShowAvailability(user.hasAvailabilityPermission())
        .isFinalUser(user.isFinalUserRole())
        .finalCustomer(user.getFinalCustomer())
        .collectionShortName(user.getCollectionShortname())
        .isShowPriceType(user.getSettings().isPriceDisplayChanged())
        .isShowFinalCustomerNetPrice(user.isFinalUserHasNetPrice())
        .customerCurrency(user.getCustomer().getCurrency())
        .isShowFinalCustomerNetPrice(user.isFinalUserHasNetPrice())
        .customerCurrency(user.getCustomer().getCurrency())
        .build();
  }

}
