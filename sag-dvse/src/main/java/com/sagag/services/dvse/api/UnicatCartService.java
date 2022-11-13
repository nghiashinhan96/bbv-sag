package com.sagag.services.dvse.api;

import java.util.Map;
import java.util.Optional;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.dvse.dto.unicat.OrderedItemDto;

/**
 * <p>
 * The service for add item to e-Connect shopping cart from UNICAT.
 * </p>
 */
public interface UnicatCartService {

  OrderedItemDto addItemsToCart(final UserInfo user, Map<String, Optional<Integer>> articleIdsAndQuantity);

}
