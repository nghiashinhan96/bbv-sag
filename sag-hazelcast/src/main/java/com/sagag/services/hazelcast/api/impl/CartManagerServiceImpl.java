package com.sagag.services.hazelcast.api.impl;

import com.google.common.base.MoreObjects;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.ShoppingCartCache;
import com.sagag.services.hazelcast.domain.cart.CartItemType;
import com.sagag.services.hazelcast.entryprocessor.AddOrUpdateCartItemEntryProcessorCache;
import com.sagag.services.hazelcast.entryprocessor.RemoveCartItemEntryProcessorCache;
import com.sagag.services.hazelcast.entryprocessor.UpdateCartItemQuantityEntryProcessorCache;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to manage the shopping cart for ESHOP online.
 */
@Service
@Slf4j
public class CartManagerServiceImpl implements CartManagerService {

  private static final String KEY_USER_ID = "items[any].userId";

  private final IMap<String, ShoppingCartCache> cartMap;

  private final IMap<String, Integer> totalMap;

  /**
   * Initializes the shopping cart manager.
   */
  @Autowired
  public CartManagerServiceImpl(final HazelcastInstance hazelcast) {
    this.cartMap = hazelcast.getMap(HazelcastMaps.SHOPPING_CART_MAP.name());
    this.totalMap = hazelcast.getMap(HazelcastMaps.TOTAL_CART_MAP.name());
    this.cartMap.addIndex(KEY_USER_ID, true);
  }

  @Override
  public void add(final CartItemDto cartItem) {
    long start = System.currentTimeMillis();

    final ExecutionCallback updateTotalCacheExeCallback = new ExecutionCallback() {

      @Override
      public void onResponse(Object response) {
        log.debug("Adding or updating cart item is success {}", response);
        updateTotalCache(cartItem.getUserId(), cartItem.getCustomerNr(), cartItem.getShopType());
      }

      @Override
      public void onFailure(Throwable t) {
        log.error("The update has error:", t);
      }
    };
    cartMap.submitToKey(UserInfo.userKey(cartItem.getUserId(), cartItem.getCustomerNr()),
        new AddOrUpdateCartItemEntryProcessorCache(cartItem.getCartKey(), cartItem),
      updateTotalCacheExeCallback);
    // it may be turn on pro to inspect, so i make it info level
    log.debug(" Perf-> add took {}", System.currentTimeMillis() - start);
  }

  @Override
  public void update(final String userKey, final String cartKey, final int quantity) {
    if (quantity == 0) { // remove
      remove(userKey, cartKey);
      return;
    }
    log.debug("Updating the quantity for cart key {}", cartKey);
    cartMap.submitToKey(userKey, new UpdateCartItemQuantityEntryProcessorCache(cartKey, quantity));
  }

  @Override
  public void remove(final String userKey, final String cartKey) {
    log.debug("Removing a cart item from cart key {}", cartKey);
    cartMap.submitToKey(userKey, new RemoveCartItemEntryProcessorCache(cartKey));
  }

  @Override
  public void clearCart(final String userId, final String customerNr, ShopType shopType) {
    String userKey = UserInfo.userKey(userId, customerNr);
    this.checkoutCart(userId, customerNr, shopType).stream()
        .filter(item -> !StringUtils.isBlank(item.getCartKey()))
        .forEach(item -> remove(userKey, item.getCartKey()));
    this.totalMap.set(totalCartKey(userKey, shopType), NumberUtils.INTEGER_ZERO);
  }

  /**
   * Returns all cart items for specific user.
   *
   * @param userId the user id who requests
   * @param customerNr the ERP customer number for that user
   * @return all items shopped by this user.
   */
  private Collection<CartItemDto> findAll(String userId, String customerNr, ShopType shopType) {
    String userKey = UserInfo.userKey(userId, customerNr);
    ShoppingCartCache shoppingCartCache = this.cartMap.get(userKey);
    return shoppingCartCache.getItems().stream()
        .filter(item -> item.getShopType() == ShopType.defaultValueOf(shopType))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<CartItemDto> findByKey(final String userKey, final String cartKey) {
    log.debug("Getting a cart item from cart key {}", cartKey);
    ShoppingCartCache shoppingCartCache = this.cartMap.get(userKey);
    return CollectionUtils.emptyIfNull(shoppingCartCache.getItems()).stream()
        .filter(cart -> StringUtils.equals(cart.getCartKey(), cartKey)).findFirst();
  }

  @Override
  public List<CartItemDto> checkoutCart(String userId, String customerNr, ShopType shopType) {
    log.debug("Returning shop cart of userId = {} - customerNr = {} - shopType = {}", userId,
        customerNr, shopType);
    final List<CartItemDto> items =
        findAll(userId, customerNr, shopType).stream().collect(Collectors.toList());

    // Because we have to sync cart map and total map for performance,
    // We will add the background task to sync total per checkout cart
    processBackgroundTasks(userId, customerNr, items, shopType);
    return items;
  }

  @Override
  public int getLatestTotalQuantity(final String userKey, final ShopType shopType) {
    return MoreObjects.firstNonNull(totalMap.get(totalCartKey(userKey, shopType)),
        NumberUtils.INTEGER_ZERO);
  }

  @Override
  @LogExecutionTime(infoMode = true)
  public void update(Map<String, CartItemDto> cartItems) {
    if (MapUtils.isEmpty(cartMap)) {
      return;
    }
    log.debug("Updating map of cart items, size of map = {}", cartItems.size());
    cartItems.forEach((cartKey, cartItem) -> {
      log.debug("Updating the cart item for cart key {}", cartKey);
      String userKey = UserInfo.userKey(cartItem.getUserId(), cartItem.getCustomerNr());
      this.cartMap.submitToKey(userKey,
        new AddOrUpdateCartItemEntryProcessorCache(cartKey, cartItem));
    });
  }

  private void processBackgroundTasks(String userId, String custNr, List<CartItemDto> cartItems,
      ShopType shopType) {
    fetchTotalMap(userId, custNr, cartItems, shopType);
  }

  // when the value is the whole ShoppingCart object, the value will be heavy
  // hash key is fast but need to load the Shopping Cart with long item list
  // so just add a value of integer total for the poll request, it should be fast
  // whatever how many items in the cart when fetching the value
  private void fetchTotalMap(String userId, String custNr, List<CartItemDto> cartItems,
      ShopType shopType) {
    long start = System.currentTimeMillis();
    final String userKey = UserInfo.userKey(userId, custNr);
    int sumOfQuantities = cartItems.stream().mapToInt(CartItemDto::getQuantity).sum();
    this.totalMap.set(totalCartKey(userKey, shopType), sumOfQuantities);
    log.debug("Perf-> updateTotal to totalMap took {}", System.currentTimeMillis() - start);
  }

  @Override
  public void updateTotalCache(String userId, String customerNr, ShopType shopType) {
    checkoutCart(userId, customerNr, shopType);
  }

  @Override
  public Collection<CartItemDto> findAllOrderPos(final String userId, final String customerNr,
      ShopType shopType) {
    String userKey = UserInfo.userKey(userId, customerNr);
    ShoppingCartCache shoppingCartCache = this.cartMap.get(userKey);
    return shoppingCartCache.getItems().stream()
        .filter(cart -> cart.getShopType() == ShopType.defaultValueOf(shopType)
            && !cart.getItemType().equals(CartItemType.DVSE_NON_REF_ARTICLE))
        .collect(Collectors.toList());
  }

  @Override
  public int countOrderPositions(String userId, String customerNr, ShopType shopType) {
    return findAllOrderPos(userId, customerNr, shopType).size();
  }

  @Override
  public void clearCartByUserIds(final List<Long> userIds) {
    final Collection<CartItemDto> cartItems = findAllByUserIds(userIds);
    if (!CollectionUtils.isEmpty(cartItems)) {
      cartItems.stream()
      .filter(item -> !StringUtils.isBlank(item.getCartKey()))
      .forEach(item -> remove(item.userKey(), item.getCartKey()));
    }
  }

  /**
   * Returns all cart items for list of users.
   *
   * @param userIds the userId list
   * @return all items shopped by these users.
   */
  private Collection<CartItemDto> findAllByUserIds(final List<Long> userIds) {
    List<String> ids =
        userIds.stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toList());
    Collection<ShoppingCartCache> shoppingCarts =
        this.cartMap.values(Predicates.in(KEY_USER_ID, ids.toArray(new String[ids.size()])));
    return shoppingCarts.stream().map(ShoppingCartCache::getItems).flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
