package com.sagag.services.dvse.api;

import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrder;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderResponse;

/**
 * <p>
 * The service for add item to e-Connect shopping cart from DVSE using TMConnect.
 * </p>
 */
public interface AxCzDvseCartService {

  SendOrderResponse addItemsToCart(ConnectUser user, SendOrder sendOrder);

}
