package com.sagag.services.dvse.api;

import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.dto.dvse.OrderedItemDto;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfItem;
import com.sagag.services.dvse.wsdl.dvse.Order;

/**
 * <p>
 * The service for add item to e-Connect shopping cart from DVSE.
 * </p>
 */
public interface DvseCartService {

  OrderedItemDto addItemsToCart(ConnectUser user, Order order, ArrayOfItem items);

}
