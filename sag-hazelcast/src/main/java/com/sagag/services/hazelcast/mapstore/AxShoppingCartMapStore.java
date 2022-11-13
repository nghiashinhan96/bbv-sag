package com.sagag.services.hazelcast.mapstore;

import com.sagag.eshop.repo.hz.entity.EshopCartItem;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.profiles.EnableShoppingCartMapStore;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.ShoppingCartCache;
import com.sagag.services.hazelcast.domain.cart.CartItemType;
import com.sagag.services.hazelcast.domain.cart.CategoryDto;
import com.sagag.services.hazelcast.repo.api.ShoppingCartItemRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to load and persist the cache to DB and vice versa.
 */
@Slf4j
@Service
@EnableShoppingCartMapStore(true)
public class AxShoppingCartMapStore extends ConnectShoppingCartMapStore {

  @Autowired
  private ShoppingCartItemRepository itemRepository;

  @Override
  public synchronized ShoppingCartCache load(String key) {
    log.debug("Loading from DB shopping cart with key = {}", key);
    String userId = extractUserIdFromUserKey(key);
    String customerNr = extractCustomerNrFromUserKey(key);
    List<EshopCartItem> cartItems = itemRepository
        .findByUserIdInAndCustomerNrIn(Arrays.asList(userId), Arrays.asList(customerNr));
    return new ShoppingCartCache(CollectionUtils.emptyIfNull(cartItems).stream()
        .map(AxShoppingCartMapStore::buildCartItem).collect(Collectors.toList()));
  }

  @Override
  public synchronized Map<String, ShoppingCartCache> loadAll(Collection<String> keys) {
    log.debug("Loading from DB all shopping cart with keys = {}", keys);
    List<String> userIds = new ArrayList<>();
    List<String> customerNrs = new ArrayList<>();
    keys.forEach(key -> {
      userIds.add(extractUserIdFromUserKey(key));
      customerNrs.add(extractCustomerNrFromUserKey(key));
    });
    List<EshopCartItem> items = itemRepository.findByUserIdInAndCustomerNrIn(userIds, customerNrs);

    return items.stream().map(AxShoppingCartMapStore::buildCartItem)
        .collect(Collectors.groupingBy(item -> item.userKey(), Collectors.toList())).entrySet()
        .stream().collect(Collectors.toMap(mapItem -> mapItem.getKey(),
            mapItem -> ShoppingCartCache.builder().items(mapItem.getValue()).build()));
  }

  // Return null since we dont need to load all carts to cache from initialization
  @Override
  public synchronized Iterable<String> loadAllKeys() {
    return null;
  }

  @Override
  public synchronized void store(String key, ShoppingCartCache shoppingCartCache) {
    List<CartItemDto> inputCartItems =
        ListUtils.defaultIfNull(shoppingCartCache.getItems(), new ArrayList<>());
    log.debug("Saving to DB shopping cart, key = {}, cart = {}", key, inputCartItems);
    String userId = extractUserIdFromUserKey(key);
    String customerNr = extractCustomerNrFromUserKey(key);
    List<EshopCartItem> existingCartItems =
        ListUtils.defaultIfNull(itemRepository.findByUserIdInAndCustomerNrIn(Arrays.asList(userId),
            Arrays.asList(customerNr)), new ArrayList<>());
    List<EshopCartItem> addedOrUpdatedItems =
        findNewOrUpdatedCartItems(inputCartItems, existingCartItems);
    List<EshopCartItem> removedCartItems = existingCartItems.stream()
        .filter(existCart -> inputCartItems.stream()
            .allMatch(inputCart -> !inputCart.getCartKey().equals(existCart.getCartKey())))
        .collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(addedOrUpdatedItems)) {
      itemRepository.saveAll(addedOrUpdatedItems);
    }
    if (CollectionUtils.isNotEmpty(removedCartItems)) {
      itemRepository.deleteAll(removedCartItems);
    }
  }

  private List<EshopCartItem> findNewOrUpdatedCartItems(List<CartItemDto> inputCartItems,
      List<EshopCartItem> existingCartItems) {
    List<EshopCartItem> addedOrUpdatedItems = new ArrayList<>();
    inputCartItems.forEach(cart -> {
      Optional<EshopCartItem> updatedOpt = existingCartItems.stream()
          .filter(existCart -> StringUtils.equals(existCart.getCartKey(), cart.getCartKey()))
          .findFirst();
      if (updatedOpt.isPresent()) {
        EshopCartItem updatedItem = updatedOpt.get();
        if (updatedItem.getQuantity() != cart.getQuantity()) {
          updatedItem.setQuantity(cart.getQuantity());
          updatedItem.setBasketItemSourceDesc(cart.getBasketItemSourceDesc());
          updatedItem.setBasketItemSourceId(cart.getBasketItemSourceId());
          addedOrUpdatedItems.add(updatedItem);
        }
      } else {
        addedOrUpdatedItems.add(buildEshopCartItem(cart));
      }
    });
    return addedOrUpdatedItems;
  }

  @Override
  public synchronized void storeAll(Map<String, ShoppingCartCache> map) {
    log.debug("Saving to DB all shopping carts, map = {}", map);
    map.entrySet().forEach(entry -> store(entry.getKey(), entry.getValue()));
  }

  @Override
  public synchronized void delete(String key) {
    String userId = extractUserIdFromUserKey(key);
    String customerNr = extractCustomerNrFromUserKey(key);
    log.debug("Deleting from DB shopping cart with key = {}", key);
    final List<EshopCartItem> existingCartItems = itemRepository
        .findByUserIdInAndCustomerNrIn(Arrays.asList(userId), Arrays.asList(customerNr));
    if (CollectionUtils.isNotEmpty(existingCartItems)) {
      itemRepository.deleteAll(existingCartItems);
    }
  }

  @Override
  public synchronized void deleteAll(Collection<String> keys) {
    log.debug("Deleting from DB all shopping carts with keys = {}", keys);
    keys.stream().forEach(this::delete);
  }

  /**
   * Builds the {@link CartItemDto} instance from the {@link EshopCartItem} record.
   *
   * @param item the eshop cart item record from DB
   * @return the cart item dto to save in cache
   */
  private static CartItemDto buildCartItem(final EshopCartItem item) {
    if (item == null) {
      return null;
    }
    final CartItemDto cartItem = new CartItemDto();
    cartItem.setCartKey(item.getCartKey());
    cartItem.setCustomerNr(item.getCustomerNr());
    cartItem.setQuantity(item.getQuantity());
    cartItem.setUserId(item.getUserId());
    cartItem.setUserName(item.getUserName());
    cartItem.setAddedTime(item.getAddedTime());
    cartItem.setArticle(SagJSONUtil.jsonToObject(item.getArticleJson(), ArticleDocDto.class));
    cartItem.setCategory(SagJSONUtil.jsonToObject(item.getCategoryJson(), CategoryDto.class));
    cartItem.setVehicle(SagJSONUtil.jsonToObject(item.getVehicleJson(), VehicleDto.class));
    cartItem.setItemType(CartItemType.valueOf(item.getItemType()));
    cartItem.setItemDesc(item.getItemDesc());
    cartItem.setAttachedArticles(
        SagJSONUtil.convertArrayJsonToList(item.getAttachedArticlesJson(), ArticleDocDto.class));
    cartItem.setShopType(ShopType.defaultValueOf(item.getShopType()));
    cartItem.setBasketItemSourceId(item.getBasketItemSourceId());
    cartItem.setBasketItemSourceId(item.getBasketItemSourceDesc());
    return cartItem;
  }

  /**
   * Builds the {@link EshopCartItem} instance from the {@link CartItemDto} record.
   *
   * @return the cart item to save into the DB
   */
  private static EshopCartItem buildEshopCartItem(CartItemDto item) {
    final EshopCartItem cartItem = new EshopCartItem();
    cartItem.setCartKey(item.getCartKey());
    cartItem.setCustomerNr(item.getCustomerNr());
    cartItem.setQuantity(item.getQuantity());
    cartItem.setUserId(item.getUserId());
    cartItem.setUserName(item.getUserName());
    cartItem.setAddedTime(item.getAddedTime());
    cartItem.setArticleJson(SagJSONUtil.convertObjectToJson(item.getArticle()));
    cartItem.setCategoryJson(SagJSONUtil.convertObjectToJson(item.getCategory()));
    cartItem.setVehicleJson(SagJSONUtil.convertObjectToJson(item.getVehicle()));
    cartItem.setItemType(item.getItemType().name());
    cartItem.setItemDesc(item.getItemDesc());
    cartItem.setAttachedArticlesJson(SagJSONUtil.convertObjectToJson(item.getAttachedArticles()));
    cartItem.setShopType(ShopType.defaultValueOf(item.getShopType()).name());
    cartItem.setBasketItemSourceId(item.getBasketItemSourceId());
    cartItem.setBasketItemSourceDesc(item.getBasketItemSourceDesc());
    return cartItem;
  }

  private static String extractUserIdFromUserKey(String userKey) {
    String[] userKeyArray = extractUserKey(userKey);
    return userKeyArray[0];
  }

  private static String extractCustomerNrFromUserKey(String userKey) {
    String[] userKeyArray = extractUserKey(userKey);
    return userKeyArray[1];
  }

  private static String[] extractUserKey(String userKey) {
    return StringUtils.split(userKey, SagConstants.UNDERSCORE);
  }

}
