package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.domain.cart.CustomShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.hazelcast.request.UpdateAmountRequestBody;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.CouponBusinessService;
import com.sagag.services.service.cart.operation.DefaultClearCartShoppingCartOperation;
import com.sagag.services.service.cart.operation.DefaultFinalInfoShoppingCartOperation;
import com.sagag.services.service.cart.operation.DefaultRemoveCartKeysShoppingCartOperation;
import com.sagag.services.service.cart.operation.DefaultUpdateCartItemShoppingCartOperation;
import com.sagag.services.service.cart.operation.UpdateDisplayedPriceShoppingCartOperation;
import com.sagag.services.service.cart.operation.UpdatePriceShoppingCartOperation;
import com.sagag.services.service.cart.operation.add.AddBuyersGuideCartFromThuleShoppingCartOperation;
import com.sagag.services.service.cart.operation.add.AddCartItemsFromArtNumbersShoppingCartOperation;
import com.sagag.services.service.cart.operation.add.AddCartItemsFromFinalOrderShoppingCartOperation;
import com.sagag.services.service.cart.operation.add.AddCartItemsFromInvoiceShoppingCartOperation;
import com.sagag.services.service.cart.operation.add.AddCartItemsFromOrderHistoryShoppingCartOperation;
import com.sagag.services.service.cart.operation.add.AddCartItemsFromSavedBasketShoppingCartOperation;
import com.sagag.services.service.cart.operation.add.DefaultAddCartItemShoppingCartOperation;
import com.sagag.services.service.request.RemoveItemRequestBody;
import com.sagag.services.service.request.UpdateDisplayedPriceRequestItem;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Implementation class for Cart business service.
 */
@Service
@Slf4j
public class CartBusinessServiceImpl implements CartBusinessService {

  private static final Void NULL_CRITERIA = null;

  @Autowired
  private CartManagerService cartManagerService;

  @Autowired
  private DefaultClearCartShoppingCartOperation clearCartOperation;

  @Autowired
  private DefaultRemoveCartKeysShoppingCartOperation removeCartKeysOperation;

  @Autowired
  private DefaultUpdateCartItemShoppingCartOperation updateCartItemOperation;

  @Autowired
  private UpdateDisplayedPriceShoppingCartOperation updateDisplayedPriceOperation;

  @Autowired
  private DefaultAddCartItemShoppingCartOperation addCartItemsOperation;

  @Autowired
  @Qualifier("defaultFinalInfoShoppingCartOperation")
  private DefaultFinalInfoShoppingCartOperation finalShopCartInfoOperation;

  @Autowired
  private UpdatePriceShoppingCartOperation updatePriceOperation;

  @Autowired
  private AddCartItemsFromArtNumbersShoppingCartOperation addCartItemFromArtNrsOperation;

  @Autowired
  private AddCartItemsFromOrderHistoryShoppingCartOperation addCartItemFromOrderHistoryOperation;

  @Autowired
  private AddCartItemsFromSavedBasketShoppingCartOperation addCartItemFromSavedBasketOpertion;

  @Autowired
  private AddCartItemsFromInvoiceShoppingCartOperation addCartItemFromInvoiceOperation;

  @Autowired
  private AddCartItemsFromFinalOrderShoppingCartOperation addCartItemFromFinalCustOrderOperation;

  @Autowired
  private AddBuyersGuideCartFromThuleShoppingCartOperation addBuyersGuideCartFromThuleShoppingCartOperation;

  @Autowired
  private CouponBusinessService couponBusService;

  @Autowired
  private EshopFavoriteService favoriteBusinessService;

  @Override
  public ShoppingCart add(final UserInfo user, List<ShoppingCartRequestBody> cartRequests,
      ShopType shopType) {
    ShoppingCart shoppingCart = addCartItemsOperation.execute(user, cartRequests, shopType);
    couponBusService.validateCoupon(user, shoppingCart);
    doUpdateFavoriteFlagIntoShoppingCartItem(user, shoppingCart);
    return shoppingCart;
  }

  @Override
  public ShoppingCart update(final UserInfo user, UpdateAmountRequestBody body, ShopType shopType) {
    ShoppingCart shoppingCart =
        updateCartItemOperation.execute(user, body, shopType, ArrayUtils.EMPTY_OBJECT_ARRAY);
    doUpdateFavoriteFlagIntoShoppingCartItem(user, shoppingCart);
    return shoppingCart;
  }

  @Override
  public ShoppingCart remove(final UserInfo user, RemoveItemRequestBody body, ShopType shopType) {
    ShoppingCart shoppingCart =
        removeCartKeysOperation.execute(user, body.getCartKeys(), shopType, body.getReloadAvail());
    doUpdateFavoriteFlagIntoShoppingCartItem(user, shoppingCart);
    return shoppingCart;
  }

  @Override
  public ShoppingCart updateDisplayedPrices(final UserInfo user,
      List<UpdateDisplayedPriceRequestItem> items, ShopType shopType) {
    return updateDisplayedPriceOperation.execute(user, items, shopType,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  @Override
  public void clear(final UserInfo user, ShopType shopType) {
    clearCartOperation.execute(user, NULL_CRITERIA, shopType, ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  @Override
  public ShoppingCart addOrderToCart(final UserInfo user, final Long orderId,
      final String orderNumber, ShopType shopType, String basketItemSourceId, String basketItemSourceDesc) {
    return addCartItemFromOrderHistoryOperation.execute(user, orderId, shopType, orderNumber,
            basketItemSourceId, basketItemSourceDesc);
  }

  @Override
  public ShoppingCart addInvoiceToCart(final UserInfo user, final String invoiceNr,
      final String orderNr, final Long orderId, ShopType shopType, String basketItemSourceId, String basketItemSourceDesc) {
    return addCartItemFromInvoiceOperation.execute(user, invoiceNr, shopType, orderNr, orderId,
            basketItemSourceId, basketItemSourceDesc);
  }

  @Override
  public ShoppingCart addSavedBasketToShoppingCart(final UserInfo user, final Long basketId,
      ShopType shopType) {
    return addCartItemFromSavedBasketOpertion.execute(user, basketId, shopType,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  @Override
  public ShoppingCart updatePricesForShoppingCart(UserInfo user, ShopType shopType) {
    return updatePriceOperation.execute(user, NULL_CRITERIA, shopType,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  @Override
  public CustomShoppingCart addMultipleArticlesByArticleNumbers(UserInfo user, String[] articleNrs,
      Integer[] quantities, ShopType shopType) {
    final Object[] objects = quantities;
    return addCartItemFromArtNrsOperation.execute(user, articleNrs, shopType, objects);
  }

  @Override
  public int countOrderPositions(final String userId, final String custNr, ShopType shopType) {
    return cartManagerService.countOrderPositions(userId, custNr, shopType);
  }

  @Override
  public ShoppingCart addFinalCustomerOrderToShoppingCart(UserInfo user, Long finalCustOrderId,
      ShopType shopType, String basketItemSourceId, String basketItemSourceDesc) {
    ShoppingCart cart = addCartItemFromFinalCustOrderOperation.execute(user, finalCustOrderId,
        shopType, basketItemSourceId, basketItemSourceDesc);
    doUpdateFavoriteFlagIntoShoppingCartItem(user, cart);
    return cart;
  }

  @Override
  public ShoppingCart checkoutShopCart(UserInfo user, ShopType shopType, boolean cacheMode,
      boolean quickViewMode) {
    if (cacheMode) {
      ShoppingCart cart = finalShopCartInfoOperation.checkoutCart(user, shopType);
      double vatRate = user.getSettings().getVatRate();
      finalShopCartInfoOperation.updateArticleInformation(cart, vatRate, user);
      couponBusService.validateCoupon(user, cart);
      doUpdateFavoriteFlagIntoShoppingCartItem(user, cart);
      return cart;
    }
    return checkoutShopCart(user, shopType, !quickViewMode);
  }

  @Override
  public ShoppingCart checkoutShopCart(UserInfo user, ShopType shopType,
      boolean isAvailabilityReq) {
    log.info("[BBV] checkoutShopCart: start");
    ShoppingCart cart =
        finalShopCartInfoOperation.execute(user, NULL_CRITERIA, shopType, isAvailabilityReq);
    log.info("[BBV] checkoutShopCart: end");
    doUpdateFavoriteFlagIntoShoppingCartItem(user, cart);
    log.info("[BBV] checkoutShopCart: Updated favorite");
    return cart;
  }

  @Override
  public ShoppingCart addBuyersGuideCartFromThule(UserInfo user,
      Map<String, String> buyersGuideFormData, ShopType shopType) {
    return addBuyersGuideCartFromThuleShoppingCartOperation.execute(user, buyersGuideFormData,
        shopType);
  }

  private void doUpdateFavoriteFlagIntoShoppingCartItem(UserInfo user,
      ShoppingCart shoppingCart) {
    final List<ArticleDocDto> articles = CollectionUtils
        .emptyIfNull(Optional.ofNullable(shoppingCart).map(ShoppingCart::getItems).orElse(null))
        .stream().map(ShoppingCartItem::getArticle).collect(Collectors.toList());

    favoriteBusinessService.updateFavoriteFlagArticles(user, articles);
  }
}
