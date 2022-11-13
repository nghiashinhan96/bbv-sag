package com.sagag.services.service.cart.callable;

import com.hazelcast.util.StringUtil;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartItemDtoBuilders;
import com.sagag.services.hazelcast.domain.cart.CartItemType;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.service.cart.CartItemUtils;
import com.sagag.services.service.cart.operation.UpdateAttachedArticleShoppingCartOperation;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateCartItemCallableCreatorImpl
    implements CallableCreator<ShoppingCartRequestBody, Void> {

  @Autowired
  protected CartManagerService cartManagerService;

  @Autowired
  private UpdateAttachedArticleShoppingCartOperation updateAttachedArticleShoppingCartOperation;

  @SuppressWarnings("unchecked")
  @Override
  @LogExecutionTime(infoMode = true)
  public Callable<Void> create(ShoppingCartRequestBody cartRequest,
      Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      final UserInfo user = (UserInfo) objects[1];
      final Date addedTime = (Date) objects[2];
      final ShopType shopType = (ShopType) objects[3];
      final List<CartItemDto> items = (List<CartItemDto>) objects[4];
      createCartItem(user, cartRequest, addedTime, shopType, items);
      return null;
    };
  }

  private void createCartItem(UserInfo user, ShoppingCartRequestBody cartRequest,
      Date addedTime, ShopType shopType, List<CartItemDto> items) {
    final String vehId = cartRequest.vehId();
    final String idSagsys = cartRequest.idSagsys();
    log.debug("Start create cart item vehicleId = {}, idSagsys = {}", vehId, idSagsys);
    final ArticleDocDto art = cartRequest.getArticle();
    final String cartItemKey =
        CartKeyGenerators.cartKey(user.key(), vehId, idSagsys, art.getSupplierId(),
            art.getSupplierArticleNumber(), shopType);
    final Optional<CartItemDto> cacheCartItem = cartManagerService.findByKey(user.key(), cartItemKey);

    CartItemDto addedCartItem;
    if (cacheCartItem.isPresent()) {
      addedCartItem = cacheCartItem.get();
      // #6753 Add missing info for VIN package
      if (art.isVin()) {
        addedCartItem.getArticle().setVin(art.isVin());
        addedCartItem.getArticle().setItemDesc(art.getItemDesc());
        addedCartItem.setItemType(CartItemType.VIN);
        addedCartItem.setItemDesc(art.getItemDesc());
      }
      addedCartItem.setShopType(shopType);
      mergeAddingItem(addedCartItem, cartRequest);
      addedCartItem.setBasketItemSourceId(StringUtils.defaultIfEmpty(
          addedCartItem.getBasketItemSourceId(), cartRequest.getBasketItemSourceId()));
      addedCartItem.setBasketItemSourceDesc(StringUtils.defaultIfEmpty(
          addedCartItem.getBasketItemSourceDesc(), cartRequest.getBasketItemSourceDesc()));
    } else {
      addedCartItem = CartItemDtoBuilders.build(cartItemKey, user, cartRequest, shopType);
    }

    addedCartItem.setAddedTime(addedTime);
    if (CartItemUtils.showPfandArticleCase(user.getCustomer(), art)
        || CartItemUtils.hasNormalAttachedArticle(art)) {
      updateAttachedArticleShoppingCartOperation.execute(user, addedCartItem, shopType);
    }

    cartManagerService.add(addedCartItem);

    doModifyCurrentShoppingCartItems(items, addedCartItem, cartItemKey);

    log.debug("End create cart item vehicleId = {}, idSagsys = {}", vehId, idSagsys);
  }

  private void doModifyCurrentShoppingCartItems(List<CartItemDto> items, CartItemDto addedCartItem,
      String cartItemKey) {
    Optional<CartItemDto> existingItem = items.stream()
        .filter(item -> StringUtil.equalsIgnoreCase(item.getCartKey(), cartItemKey)).findFirst();
    if (existingItem.isPresent()) {
      int indexOfExistedItem = items.indexOf(existingItem.get());
      items.set(indexOfExistedItem, addedCartItem);
    } else {
      items.add(addedCartItem);
    }
  }

  private void mergeAddingItem(CartItemDto existingCartItem, ShoppingCartRequestBody cartRequest) {
    if (cartRequest.isOverideExisting()) {
      existingCartItem.setQuantity(cartRequest.getQuantity());
      final ArticleDocDto cartRequestArticle = cartRequest.getArticle();
      if (Objects.isNull(cartRequestArticle.getDisplayedPrice())
          && Objects.nonNull(existingCartItem.getArticle())) {
        cartRequestArticle.setDisplayedPrice(existingCartItem.getArticle().getDisplayedPrice());
      }
      existingCartItem.setArticle(cartRequestArticle);
      return;
    }
    // #1697
    int mergeQuantity = cartRequest.getQuantity() + existingCartItem.getQuantity();
    existingCartItem.setQuantity(mergeQuantity);
  }
}
