package com.sagag.services.service.order.steps;

import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderItem;
import com.sagag.services.service.utils.OrdersTestUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class CreateOrderRequestBodyV2Test {

  @Mock
  private List<OrderItem> items = OrdersTestUtils.createSampleListOrderItem();

  @Mock
  CreateOrderRequestBodyV2 createOrderRequestBodyV2;

  @Test
  public void test_getAdditionalTextDocs() {
    Map<String, String> expextedAdditionTextDocs = items.stream()
        .collect(Collectors.toMap(OrderItem::getCartKey, OrderItem::getAdditionalTextDoc));
    final Map<String, String> additionalTextDocs = createOrderRequestBodyV2.getAdditionalTextDocs();
    Assert.assertEquals(expextedAdditionTextDocs, additionalTextDocs);
  }
}
