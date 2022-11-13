package com.sagag.services.dvse.dto.unicat;


import com.sagag.services.dvse.wsdl.unicat.ArrayOfItem;
import com.sagag.services.dvse.wsdl.unicat.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedItemDto {

  private Order order;

  private ArrayOfItem addedItems;

  public OrderedItemDto(String orderId) {
    final Order resultOrder = new Order();
    resultOrder.setOrderId(orderId);
    this.order = resultOrder;
  }

}
