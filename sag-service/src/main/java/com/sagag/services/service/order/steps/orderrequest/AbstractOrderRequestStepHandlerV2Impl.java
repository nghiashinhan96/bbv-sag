package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.availability.MultipleGroupArticleAvailabilitiesSplitter;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.ax.enums.AxOrderType;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.ExecutionOrderType;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.order.steps.GlassBodyOrWorkMessageOrderRequestStepHandler;
import com.sagag.services.service.order.steps.InitializeOrderRequestStepHandler;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderContextBuilder;
import com.sagag.services.service.utils.order.OrderRequestBuilderV2;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class AbstractOrderRequestStepHandlerV2Impl implements InitializeOrderRequestStepHandler {

  private static final String VIRTUAL_WAREHOUSE_WITHOUT_INTERFACE = "VWH";

  private static final int SINGLE_GROUP = 1;

  private static final String LOG_INFO = "Initializing order request with body = {}";

  @Autowired
  private GlassBodyOrWorkMessageOrderRequestStepHandler glassBodyWorkMsgOrderRequestStepHandler;

  @Autowired
  private OrderingOptimizer orderingOptimizer;

  @Autowired
  private MultipleGroupArticleAvailabilitiesSplitter multipleGroupArticleAvailabilitiesSplitter;

  @Override
  public ExternalOrderRequest handle(UserInfo user, SupportedAffiliate affiliate,
      String axOrderType, List<ShoppingCartItem> cartItems, OrderContextBuilder orderBuilder,
      ExecutionOrderType orderExecutionType) {
    CreateOrderRequestBodyV2 body = orderBuilder.getBody();
    log.info(LOG_INFO, body);

    final OrderRequestBuilderV2 orderRequestBuilder = new OrderRequestBuilderV2();
    orderRequestBuilder.setAffiliate(affiliate);
    orderRequestBuilder.setCustNr(user.getCustNrStr());
    // Update order type to send to AX if exists.
    orderRequestBuilder
        .setOrderType(overrideOrderTypeForSpecialCase(axOrderType, affiliate, cartItems));

    orderRequestBuilder.setSalesOnBehalf(user.isSaleOnBehalf());
    orderRequestBuilder.setSalesUsername(StringUtils.defaultString(user.getSalesUsername()));
    orderRequestBuilder.setCartItems(correctAvais(
        body.isCompleteDelivery() ? orderingOptimizer.optimize(cartItems) : cartItems));
    orderRequestBuilder.setRequest(body);
    orderRequestBuilder.setDefaultBranchId(user.getDefaultBranchId());
    orderRequestBuilder.setCouponUseLog(orderBuilder.getCoupon().getCouponUseLog());
    orderRequestBuilder.setOrderFrom(body.getOrderFrom());

    orderRequestBuilder.setOrderCondition(orderBuilder.getSelectedOrderCondition());
    ExternalOrderRequest externalOrderRequest = orderRequestBuilder.build();

    glassBodyWorkMsgOrderRequestStepHandler.handle(affiliate, externalOrderRequest, cartItems,
        body.getRequestDateTime());

    return externalOrderRequest;
  }

  // TODO @quiluc this is to override the orderType for CZAX10 if the order is from Beethoven
  // Need to be updated base on additional point in ticket 5830
  private String overrideOrderTypeForSpecialCase(String axOrderType, SupportedAffiliate affiliate,
      List<ShoppingCartItem> cartItems) {
    String companyName = affiliate.getCompanyName();
    String sagCZ = com.sagag.services.common.enums.SupportedAffiliate.SAG_CZECH.getCompanyName();
    List<String> swissAffiliates = Arrays.asList(
        com.sagag.services.common.enums.SupportedAffiliate.DERENDINGER_CH.getCompanyName(),
        com.sagag.services.common.enums.SupportedAffiliate.MATIK_CH.getCompanyName(),
        com.sagag.services.common.enums.SupportedAffiliate.TECHNOMAG.getCompanyName(),
        com.sagag.services.common.enums.SupportedAffiliate.WBB.getCompanyName());

    if (axOrderType.equalsIgnoreCase(AxOrderType.KSO_AUT.name())) {
      if (StringUtils.equals(companyName, sagCZ)) {
        return selectSpecialOrderTypeForCZ(cartItems);
      } else if (swissAffiliates.contains(companyName)) {
        return selelectSpecialOrderTypeForCH(cartItems);
      }
    }
    return axOrderType;
  }

  private String selelectSpecialOrderTypeForCH(List<ShoppingCartItem> cartItems) {
    List<Availability> availabilities = org.apache.commons.collections4.CollectionUtils
        .emptyIfNull(cartItems).stream().map(item -> item.getArticle().getAvailabilities())
        .flatMap(avail -> avail.stream()).collect(Collectors.toList());
    Optional<Availability> VWHavailability = org.apache.commons.collections4.CollectionUtils
        .emptyIfNull(availabilities).stream().filter(avail -> StringUtils
            .equalsIgnoreCase(VIRTUAL_WAREHOUSE_WITHOUT_INTERFACE, avail.getExternalVendorTypeId()))
        .findFirst();
    if (VWHavailability.isPresent()) {
      return AxOrderType.KSO_DROP.name();
    }
    return AxOrderType.KSO_AUT.name();
  }

  private String selectSpecialOrderTypeForCZ(List<ShoppingCartItem> cartItems) {
    List<List<Availability>> avails =
        org.apache.commons.collections4.CollectionUtils.emptyIfNull(cartItems).stream()
            .map(item -> item.getArticleItem().getAvailabilities()).collect(Collectors.toList());
    List<Availability> flatAvais = org.apache.commons.collections4.CollectionUtils
        .emptyIfNull(avails).stream().flatMap(avail -> avail.stream()).collect(Collectors.toList());
    Map<String, Long> vendorTypeAndId =
        org.apache.commons.collections4.CollectionUtils.emptyIfNull(flatAvais).stream()
            .filter(avail -> Objects.nonNull(avail.getVendorId())
                && Objects.nonNull(avail.getExternalVendorTypeId()))
            .collect(Collectors.toMap(Availability::getExternalVendorTypeId,
                Availability::getVendorId, (oldVendorId, newVendorId) -> newVendorId));
    if (vendorTypeAndId.size() == 1) {
      Optional<String> value = vendorTypeAndId.keySet().stream().findFirst();
      if (value.isPresent()) {
        String externalVendorTypeId = value.get();
        if (StringUtils.equalsIgnoreCase(externalVendorTypeId,
            VIRTUAL_WAREHOUSE_WITHOUT_INTERFACE)) {
          return AxOrderType.KSO_T.name();
        }
      }
    } else if (vendorTypeAndId.size() > 1) {
      Optional<String> vwhType = vendorTypeAndId.keySet().stream()
          .filter(v -> StringUtils.equalsIgnoreCase(v, VIRTUAL_WAREHOUSE_WITHOUT_INTERFACE))
          .findFirst();
      if (vwhType.isPresent()) {
        return AxOrderType.STD_KSO.name();
      }

    }
    return AxOrderType.KSO_BEET.name();
  }

  private List<ShoppingCartItem> correctAvais(List<ShoppingCartItem> cartItems) {
    if (CollectionUtils.isEmpty(cartItems)) {
      return Collections.emptyList();
    }

    Map<String, List<ShoppingCartItem>> articleGroups =
        cartItems.stream().collect(Collectors.groupingBy(ShoppingCartItem::getIdSagsys));

    Map<String, List<ShoppingCartItem>> multipleGroupArticles = articleGroups.entrySet().stream()
        .filter(entry -> CollectionUtils.isNotEmpty(entry.getValue())
            && entry.getValue().size() > SINGLE_GROUP)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    if (MapUtils.isEmpty(multipleGroupArticles)) {
      return cartItems;
    }

    multipleGroupArticles.entrySet()
        .forEach(entry -> multipleGroupArticleAvailabilitiesSplitter.splitAvailabilities(entry
            .getValue().stream().map(ShoppingCartItem::getArticle).collect(Collectors.toList())));
    return cartItems;
  }
}
