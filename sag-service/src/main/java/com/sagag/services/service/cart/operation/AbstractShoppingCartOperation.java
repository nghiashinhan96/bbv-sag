package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.api.VatRateCacheService;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.service.api.CouponBusinessService;
import com.sagag.services.service.cart.CartItemUtils;
import com.sagag.services.service.cart.CartResetDisplayPriceHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractShoppingCartOperation<T, R> implements ShoppingCartOperation<T, R> {

  @Autowired
  private CartManagerService cartManagerService;

  @Autowired
  protected IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  protected CouponBusinessService couponBusService;

  @Autowired
  private VatRateCacheService vatRateCacheService;

  @Autowired
  private CartResetDisplayPriceHelper cartResetDisplayPriceHelper;

  @Override
  public CartManagerService cartManager() {
    return cartManagerService;
  }

  @LogExecutionTime(infoMode = true)
  public ShoppingCart checkoutCart(UserInfo user, ShopType shopType) {
    return doCheckoutCart(user, shopType);
  }

  public ShoppingCart doCheckoutCart(UserInfo user, Collection<CartItemDto> items) {
    final Customer customer = user.getCustomer();
    Map<String, Double> artVatRateMap = vatRateCacheService
        .getCacheVatRateByArticleIds(items.stream().map(CartItemDto::getArticle)
            .map(ArticleDocDto::getArtid).collect(Collectors.toList()))
        .stream().collect(Collectors.toMap(VatRateDto::getArtId, VatRateDto::getCustomVatRate));

    final double defaultVatRate = user.getSettings().getVatRate();

    final List<ShoppingCartItem> cartItems = items.stream().map(item -> {
      String atrId = item.getArticle().getArtid();
      Double vatRate =
          Objects.isNull(artVatRateMap.get(atrId)) ? defaultVatRate : artVatRateMap.get(atrId);
      return ShoppingCartItem.itemConverter().apply(item, vatRate);
    }).collect(Collectors.toList());

    ShoppingCart shoppingCart = new ShoppingCart(cartItems);
    CartItemUtils.bindAttachedArticleItems(shoppingCart, defaultVatRate, customer);

    cartResetDisplayPriceHelper.accept(user.getSettings().isPriceDisplayChanged(), shoppingCart);

    return shoppingCart;
  }

  private ShoppingCart doCheckoutCart(UserInfo user, ShopType shopType) {
    final String userId = user.getCachedUserId();
    final double defaultVatRate = user.getSettings().getVatRate();
    final String custNr = user.getCustNr();
    log.debug("Checking out shopping cart with cachedUser = {}, custNr = {} and vatRate = {}",
        userId, custNr, defaultVatRate);

    final Collection<CartItemDto> items = cartManagerService.checkoutCart(userId, custNr, shopType);
    return doCheckoutCart(user, items);
  }

  @LogExecutionTime
  @Deprecated
  protected ShoppingCart reloadShoppingCart(final UserInfo user, final ShopType shopType) {
    return reloadShoppingCart(user, doCheckoutCart(user, shopType));
  }

  @LogExecutionTime
  protected ShoppingCart reloadShoppingCart(final UserInfo user, final ShoppingCart shoppingCart) {
    final double vatRate = user.getSettings().getVatRate();


    final Map<String, VehicleDto> vehicleByVehId = getVehicleByVehId(shoppingCart);
    final Map<String, List<ShoppingCartItem>> artsByVehId = prepareSyncShoppingItems(shoppingCart);

    for (final Entry<String, List<ShoppingCartItem>> artEntry : artsByVehId.entrySet()) {
      final VehicleDto vehDoc = vehicleByVehId.get(artEntry.getKey());
      final List<ShoppingCartItem> items = artEntry.getValue();
      final List<ArticleDocDto> allDocs =
          items.stream().map(ShoppingCartItem::getArticleItem).collect(Collectors.toList());
      ivdsArticleTaskExecutors.executeTaskWithoutErp(user, allDocs, Optional.ofNullable(vehDoc));
    }

    CartItemUtils.bindAttachedArticleItems(shoppingCart, vatRate, user.getCustomer());

    return shoppingCart;
  }

  protected Map<String, VehicleDto> getVehicleByVehId(final ShoppingCart shoppingCart) {
    return shoppingCart.getItems().stream().filter(item -> !item.isNonReference())
        .collect(Collectors.toMap(ShoppingCartItem::getVehicleId, ShoppingCartItem::getVehicle,
            (veh1, veh2) -> veh1));
  }

  protected Map<String, List<ShoppingCartItem>> prepareSyncShoppingItems(
      final ShoppingCart shoppingCart) {
    // update article amount number to pass to ERP price & availability request
    updateArticleAmountNumber(shoppingCart);
    // the api to get prices for a list of articles within a vehicle
    return shoppingCart.getItems().stream().filter(item -> !item.isNonReference())
        .collect(Collectors.groupingBy(ShoppingCartItem::getVehicleId));
  }

  private static void updateArticleAmountNumber(final ShoppingCart shoppingCart) {
    shoppingCart.getItems().stream()
    .filter(itm -> !Objects.isNull(itm) && !Objects.isNull(itm.getArticle()))
    .forEach(item -> item.getArticle().setAmountNumber(item.getQuantity()));
  }

  protected void doSyncLatestArticlesIntoCache(final String cachedUserId, String customerNr,
    final String userKey, final ShoppingCart shoppingCart, final ShopType shopType,
    final String vehId, final String idSagSys) {
    final Collection<CartItemDto> cartItems =
        cartManagerService.checkoutCart(cachedUserId, customerNr, shopType);
    final Map<String, ShoppingCartItem> cartItemMap = buildShoppingCartItems(userKey,
        shoppingCart, shopType);

    final Map<String, CartItemDto> cachedCartItemMap = new HashMap<>();
    cartItems.forEach(cartItem -> {
      final String cartKey = cartItem.getCartKey();
      ShoppingCartItem shoppingCartItem;
      if (StringUtils.isBlank(vehId) || StringUtils.isBlank(idSagSys)) {
        shoppingCartItem = cartItemMap.get(cartKey);
      } else {
        shoppingCartItem = cartItemMap.get(cartKey);
        if (!shoppingCartItem.equalsArticleId(idSagSys)
          || !shoppingCartItem.equalsVehicleId(vehId)) {
          shoppingCartItem = null;
        }
      }

      if (shoppingCartItem != null) {
        final ArticleDocDto articleDoc = shoppingCartItem.getArticle();
        final List<ArticleDocDto> attachedArticles = shoppingCartItem.getAttachedArticles();
        cartItem.setAttachedArticles(attachedArticles);
        if (Objects.nonNull(articleDoc)) {
          cartItem.setArticle(articleDoc);
          cachedCartItemMap.put(cartKey, cartItem);
        }
      }
    });

    cartManagerService.update(cachedCartItemMap);
  }

  private static Map<String, ShoppingCartItem> buildShoppingCartItems(final String userKey,
      final ShoppingCart shoppingCart, final ShopType shopType) {
    final Map<String, ShoppingCartItem> cartItemMap = new HashMap<>();
    shoppingCart.getItems()
    .forEach(item -> cartItemMap.putIfAbsent(buildKeyAvailabilityMap(userKey, item, shopType), item));
    return cartItemMap;
  }

  private static String buildKeyAvailabilityMap(final String userKey,
    final ShoppingCartItem cartItem, final ShopType shopType) {
    final String vehId =
        cartItem.getVehicle() == null ? StringUtils.EMPTY : cartItem.getVehicle().getVehId();
    final ArticleDocDto art = cartItem.getArticle();
    return CartKeyGenerators.cartKey(userKey, vehId, art.getIdSagsys(), art.getSupplierId(),
        art.getSupplierArticleNumber(), shopType);
  }

  public void updateArticleInformation(ShoppingCart shoppingCart, double defaultVatRate,
    UserInfo user) {
    updateVatRateForArticle(defaultVatRate, shoppingCart);

    CartItemUtils.bindAttachedArticleItems(shoppingCart, defaultVatRate, user.getCustomer());
  }

  protected void updateVatRateForArticle(Double defaultVatRate, ShoppingCart cart) {
    Map<String, Double> artVatRateMap = vatRateCacheService
      .getCacheVatRateByArticleIds(cart.getItems().stream().map(ShoppingCartItem::getArticleItem)
        .map(ArticleDocDto::getArtid).collect(Collectors.toList()))
      .stream().collect(Collectors.toMap(VatRateDto::getArtId, VatRateDto::getCustomVatRate));

    cart.getItems().forEach(cartItem -> {
      String atrId = cartItem.getArticleItem().getArtid();
      Double vatRate =
        Objects.isNull(artVatRateMap.get(atrId)) ? defaultVatRate : artVatRateMap.get(atrId);
      cartItem.setPriceItem(cartItem.getArticleItem(), vatRate);
    });
  }
}
