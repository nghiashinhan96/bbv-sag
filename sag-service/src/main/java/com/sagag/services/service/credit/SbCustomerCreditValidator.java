package com.sagag.services.service.credit;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.eshop.dto.CustomerCreditCheckResultDto;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.order.handler.SbCartTypeHander;
import com.sagag.services.service.order.model.LocationTypeFilter;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@SbProfile
public class SbCustomerCreditValidator implements CustomerCreditValidator {

  @Autowired
  private CartBusinessService cartBusService;

  @Autowired
  private SbCartTypeHander sbCartTypeHandler;

  @Autowired
  private ContextService contextService;

  @Override
  public CustomerCreditCheckResultDto validate(UserInfo user, double customerCredit,
      double customerCashCreditPayment, ShopType shopType) {
    Assert.notNull(user, "User must not be null");
    Assert.isTrue(user.isValidCustNr(), "Invalid ERP customer number");
    Assert.notNull(user.getCollectionId(), "Invalid Collection id");
    Assert.hasText(user.getAffiliateShortName(), "Affiliate name is required");
    Assert.hasText(user.getUsername(), "Username is required");

    final ShoppingCart shoppingCart = cartBusService.checkoutShopCart(user, shopType, true);
    Map<LocationTypeFilter, ShoppingCart> cartByLocation =
        sbCartTypeHandler.handleShoppingCart(shoppingCart, user, false);

    final EshopBasketContext basketContext = contextService.getBasketContext(user.key());
    final Map<GrantedBranchDto, PaymentMethodDto> paymentMethods =
        getPaymentMethodByLocation(basketContext);

    Map<PaymentMethodDto, List<ShoppingCart>> shoppingCartByPaymentMethod = new HashMap<>();
    MapUtils.emptyIfNull(cartByLocation).entrySet().forEach(cart -> {
      Entry<GrantedBranchDto, PaymentMethodDto> paymentMethod =
          getPaymentMethodByLocationOrDefault(paymentMethods, cart.getKey(), basketContext);
      shoppingCartByPaymentMethod.computeIfAbsent(paymentMethod.getValue(), k -> new ArrayList<>())
          .add(cart.getValue());
    });
    List<CustomerCreditCheckResultDto> creditCheckResultByPaymentMethod = new ArrayList<>();

    shoppingCartByPaymentMethod.entrySet().forEach(sp -> {
      PaymentMethodType paymentMethod =
          PaymentMethodType.valueOfIgnoreCase(sp.getKey().getDescCode());
      double totalCartAmount =
          sp.getValue().stream().mapToDouble(this::recalculateNetTotalExclVat).sum();
      creditCheckResultByPaymentMethod.add(CustomerCreditCheckResultDto.builder()
          .paymentMethod(paymentMethod.name())
          .valid(isValidCreditAmount(paymentMethod, customerCredit, customerCashCreditPayment,
              totalCartAmount))
          .customerCredit(getCheckAmount(paymentMethod, customerCredit, customerCashCreditPayment))
          .build());
    });

    return CustomerCreditCheckResultDto.builder()
        .creditCheckResultByPaymentMethod(creditCheckResultByPaymentMethod).build();
  }

  private Entry<GrantedBranchDto, PaymentMethodDto> getPaymentMethodByLocationOrDefault(
      final Map<GrantedBranchDto, PaymentMethodDto> paymentMethods,
      LocationTypeFilter locationFilter, EshopBasketContext basketContext) {
    return paymentMethods.entrySet().stream()
        .filter(pm -> pm.getKey().getBranchId()
            .equalsIgnoreCase(locationFilter.getLocation().getBranchId()))
        .findFirst().orElse(new AbstractMap.SimpleEntry<>(locationFilter.getLocation(),
            basketContext.getPaymentMethod()));
  }

  private double recalculateNetTotalExclVat(ShoppingCart shoppingCart) {
    return shoppingCart.getItems().parallelStream().mapToDouble(
        item -> item.getNetPrice() * item.getQuantity() + item.getAttachedArticleTotalNetPrice())
        .sum();
  }

  private double getCheckAmount(PaymentMethodType paymentMethod, double customerCredit,
      double customerCashCreditPayment) {
    if (paymentMethod == PaymentMethodType.CASH) {
      return customerCashCreditPayment;
    }
    return customerCredit;
  }

  private boolean isValidCreditAmount(PaymentMethodType paymentMethod, double customerCredit,
      double customerCashCreditPayment, double totalCartAmount) {
    if (paymentMethod == PaymentMethodType.CASH) {
      return customerCashCreditPayment == 0 || totalCartAmount <= customerCashCreditPayment;
    }
    return customerCredit == 0 || totalCartAmount <= customerCredit;
  }

  private Map<GrantedBranchDto, PaymentMethodDto> getPaymentMethodByLocation(
      final EshopBasketContext basketContext) {
    if (basketContext == null || basketContext.getPaymentMethod() == null) {
      return Collections.emptyMap();
    }
    return ListUtils.emptyIfNull(basketContext.getEshopBasketContextByLocation()).stream()
        .filter(t -> !Objects.isNull(t.getLocation())).collect(Collectors
            .toMap(EshopBasketContext::getLocation, EshopBasketContext::getPaymentMethod));
  }
}
