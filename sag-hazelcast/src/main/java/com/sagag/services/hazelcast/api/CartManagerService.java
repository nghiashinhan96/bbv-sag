package com.sagag.services.hazelcast.api;

import com.sagag.eshop.repo.hz.entity.*;
import com.sagag.services.hazelcast.domain.*;
import com.sagag.services.hazelcast.domain.cart.*;
import java.util.*;
import org.apache.commons.lang3.*;

/**
 * Cache manager service APIs for the shopping basket.
 */
public interface CartManagerService {

  /**
   * Adds an item into shopping cart.
   *
   * @param cartItem the cart item to add
   */
  void add(final CartItemDto cartItem);

  /**
   * Updates the quantity of a cart item.
   *
   * @param userKey as the key of the cache
   * @param cartKey the cart key
   * @param quantity new quantity of the cart item to update
   */
  void update(final String userKey, String cartKey, final int quantity);

  /**
   * Remove an item from the shopping cart from key.
   *
   * @param userKey order userId as key for cache
   * @param cartKey the cart key
   */
  void remove(final String userKey, final String cartKey);

  /**
   * Clears all cart items in the shopping cart for specific user.
   *
   * @param userId the user id who requests
   * @param customerNr the ERP customer number for that user
   */
  void clearCart(final String userId, final String customerNr, ShopType shopType);

  /**
   * Returns the cart item by key.
   *
   * @param userKey as the key of the cache
   * @param cartKey the cart item key.
   * @return the cart item.
   */
  Optional<CartItemDto> findByKey(final String userKey, final String cartKey);

  /**
   * Checkouts the shopping cart for specific user.
   *
   * @param userId the user id who requests
   * @param customerNr the ERP customer number for that user
   * @return the {@link ShoppingCart}
   */
  List<CartItemDto> checkoutCart(String userId, String customerNr, ShopType shopType);

  /**
   * Returns latest total number from users cart.
   *
   * @param userKey the unique user key
   * @return the latest total number of the shopping basket
   */
  int getLatestTotalQuantity(final String userKey, ShopType shopType);

  /**
   * Updates the cart item map.
   *
   * @param cartItems the map of cart items
   */
  void update(Map<String, CartItemDto> cartItems);

  /**
   * Updates total quantity for user login by DVSE.
   *
   * @param userId the user id who requests
   * @param customerNr the ERP customer number for that user
   */
  void updateTotalCache(String userId, String customerNr, ShopType shopType);

  /**
   * Returns the number of order positions in the shopping cart for specific user.
   *
   * @param userId the user id who requests
   * @param customerNr the ERP customer number for that user
   * @return the number of order positions
   */
  int countOrderPositions(String userId, String customerNr, ShopType shopType);

  /**
   * Returns all order position items for specific user.
   *
   * @param userId the user id who requests
   * @param customerNr the ERP customer number for that user
   * @return all order position items shopped by this user.
   */
  Collection<CartItemDto> findAllOrderPos(String userId, String customerNr, ShopType shopType);

  /**
   * Clears all cart items in the shopping cart for specific user.
   *
   * @param userIds the user list who requested
   */
  void clearCartByUserIds(List<Long> userIds);

  default String cartKey(CartItemDto cartItem) {
    return StringUtils.EMPTY;
  }

  default String totalCartKey(String userKey, ShopType shopType) {
    return userKey + shopType.name();
  }
}
