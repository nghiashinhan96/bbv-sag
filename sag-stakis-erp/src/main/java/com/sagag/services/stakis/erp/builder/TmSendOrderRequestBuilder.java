package com.sagag.services.stakis.erp.builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.stakis.erp.domain.TmBasketPosition;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfOrder;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfOrderId;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfOrderPosition;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfSelectionList;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfUserDefinedData;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EMasterDataType;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EntityLink;
import com.sagag.services.stakis.erp.wsdl.tmconnect.MasterData;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Order;
import com.sagag.services.stakis.erp.wsdl.tmconnect.OrderCollection;
import com.sagag.services.stakis.erp.wsdl.tmconnect.OrderId;
import com.sagag.services.stakis.erp.wsdl.tmconnect.OrderPosition;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SendOrderRequest;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SendOrderRequestBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.UserDefinedData;

@Component
@CzProfile
public class TmSendOrderRequestBuilder
  extends AbstractTmConnectRequestBuilder<ExternalOrderRequest, JAXBElement<SendOrderRequestBody>> {

  // TM Connect Service Document: 5.3.2.2 Create Notes (DocuRef)
  private static final int DF_NOTE_TYPE = 1;

  /**
   * Builds send order request.
   *
   */
  @Override
  public JAXBElement<SendOrderRequestBody> buildRequest(TmUserCredentials credentials,
      ExternalOrderRequest criteria, Object... additionals) {
    final SendOrderRequest sendOrderRequest = objectFactory.createSendOrderRequest();

    // User credentials
    buildCredentials(sendOrderRequest, credentials);

    // Basket Position UUID Map
    final List<TmBasketPosition> positions = criteria.getBasketPositions().stream()
        .map(p -> SagBeanUtils.map(p, TmBasketPosition.class)).collect(Collectors.toList());
    final Map<String, TmBasketPosition> positionMap = buildUUIDMap(positions);

    // Vehicle UUID Map
    final List<Integer> typeIds = positions.stream().map(TmBasketPosition::getKtType)
        .filter(Objects::nonNull).collect(Collectors.toList());
    final Map<String, Integer> typeMap = buildUUIDMap(typeIds);

    // Delivery Information UUID Map
    final List<String> deliveryInfo =
        Arrays.asList(StringUtils.defaultString(criteria.getSendMethodCode()));
    final Map<String, String> deliveryMap = buildUUIDMap(deliveryInfo);

    // Master data
    final MasterData masterData = buildMasterData(positionMap, typeMap, deliveryMap);
    sendOrderRequest.setMasterData(objectFactory.createMasterData(masterData));

    // Order Collection
    final String customerRefText = StringUtils.defaultString(criteria.getCustomerRefText());
    final OrderCollection orderCollection =
        buildOrderCollection(positionMap, typeMap, deliveryMap, customerRefText);
    orderCollection.setGuid(UUID.randomUUID().toString());
    sendOrderRequest.setOrderCollection(objectFactory.createOrderCollection(orderCollection));

    final SendOrderRequestBody request = objectFactory.createSendOrderRequestBody();
    request.setRequest(objectFactory.createSendOrderRequestBodyRequest(sendOrderRequest));

    return objectFactory.createSendOrder(request);
  }

  /**
   * Builds order collection for SendOrder request.
   *
   */
  private OrderCollection buildOrderCollection(Map<String, TmBasketPosition> positionMap,
      Map<String, Integer> typeMap, Map<String, String> deliveryMap, String customerRefText) {
    // Order Positions
    final ArrayOfOrderPosition arrayOfPosition = objectFactory.createArrayOfOrderPosition();
    arrayOfPosition.getOrderPosition().addAll(
        buildOrderPositions(positionMap, typeMap));

    final Order order = objectFactory.createOrder();
    order.setGuid(UUID.randomUUID().toString());
    order.setItems(objectFactory.createOrderItems(arrayOfPosition));

    // Delivery
    if (!MapUtils.isEmpty(deliveryMap)) {
      final ArrayOfSelectionList arrayOfSelectionList = buildDelivery(deliveryMap);
      order.setSelectionLists(
          objectFactory.createOrderPositionSelectionLists(arrayOfSelectionList));
    }

    final ArrayOfOrder arrayOfOrder = objectFactory.createArrayOfOrder();
    arrayOfOrder.getOrder().add(order);

    // Order Id
    final OrderId orderId = objectFactory.createOrderId();
    orderId.setType(0);
    orderId.setValue(objectFactory.createOrderIdValue(UUID.randomUUID().toString()));

    final ArrayOfOrderId orderIds = objectFactory.createArrayOfOrderId();
    orderIds.getOrderId().add(orderId);

    order.setOrderIds(objectFactory.createOrderCollectionOrderIds(orderIds));

    // Message Notes
    final UserDefinedData userDefinedData = objectFactory.createUserDefinedData();
    userDefinedData.setType(DF_NOTE_TYPE);
    userDefinedData.setValue(objectFactory.createUserDefinedDataValue(customerRefText));

    final ArrayOfUserDefinedData arrayOfUserDefinedData =
        objectFactory.createArrayOfUserDefinedData();
    arrayOfUserDefinedData.getUserDefinedData().add(userDefinedData);
    order.setUserDefinedData(
        objectFactory.createOrderPositionUserDefinedData(arrayOfUserDefinedData));

    // Order Collection
    final OrderCollection orderCollection = objectFactory.createOrderCollection();

    // orderCollection.setStatus(1);
    orderCollection.setCreateDate(DateUtils.newXMLGregorianCalendar(Instant.now()));
    orderCollection.setOrders(objectFactory.createOrderCollectionOrders(arrayOfOrder));

    return orderCollection;
  }

  private List<OrderPosition> buildOrderPositions(Map<String, TmBasketPosition> positionMap,
      Map<String, Integer> typeMap) {
    final Function<Entry<String, TmBasketPosition>, OrderPosition> mappingFunction = entry -> {
      final TmBasketPosition bPosition = entry.getValue();

      final Entry<String, Integer> vehicleEntry = typeMap.entrySet().stream()
          .filter(e -> NumberUtils.compare(e.getValue(), bPosition.getKtType()) == 0)
          .findFirst().orElse(null);
      return orderPositionConverter(vehicleEntry).apply(entry);
    };
    return positionMap.entrySet().stream().map(mappingFunction).collect(Collectors.toList());
  }

  private Function<Entry<String, TmBasketPosition>, OrderPosition> orderPositionConverter(
      Entry<String, Integer> vehicleEntry) {
    return entry -> {
      final OrderPosition oPosition = objectFactory.createOrderPosition();
      oPosition.setGuid(UUID.randomUUID().toString());

      final EntityLink articleItem = objectFactory.createEntityLink();
      final String uuid = entry.getKey();
      articleItem.setGuid(uuid);
      articleItem.setType(EMasterDataType.ARTICLE_TMF);
      oPosition.setItem(objectFactory.createOrderPositionItem(articleItem));

      final BasketPosition bPosition = entry.getValue();
      oPosition.setRequestedQuantity(BigDecimal.valueOf(bPosition.getQuantity()));
      oPosition.setConfirmedQuantity(BigDecimal.ZERO);

      // Vehicle
      if (vehicleEntry != null) {
        final EntityLink vehicleItem = buildVehicle(vehicleEntry);
        oPosition.setVehicle(objectFactory.createOrderPositionVehicle(vehicleItem));
      }

      return oPosition;
    };
  }

}
