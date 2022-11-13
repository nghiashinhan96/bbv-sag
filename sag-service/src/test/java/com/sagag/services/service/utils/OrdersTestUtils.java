package com.sagag.services.service.utils;

import com.sagag.services.service.request.order.OrderItem;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to define utility test methods for Orders.
 */
@UtilityClass
public final class OrdersTestUtils {

  public static List<OrderItem> createSampleListOrderItem() {
    return Stream
        .of(new OrderItem("367510_1000_NONVEHICLE_1000862202", "text 1"),
            new OrderItem("367510_1000_V125249M31806_1000862202", "text 2"),
            new OrderItem("367510_1000_V122653M20488_1000862202", "text 3"))
        .collect(Collectors.toList());
  }
}
