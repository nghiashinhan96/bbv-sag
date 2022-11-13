package com.sagag.services.dvse.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchExternalExecutor;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.dvse.config.SoapProperties;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.notify.TotalCartNotification;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartItemDtoBuilders;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DvseCartHandler {

  private static final String DVSE_BASKET_ITEM_SOURCE_DESC = "dvse-catalog";

  @Autowired
  private SoapProperties soapProps;

  @Autowired
  private TotalCartNotification totalCartNotification;

  @Autowired
  private CartManagerService cartManagerService;

  @Autowired
  private AttachedArticleSearchExternalExecutor<Map<String, ArticleDocDto>> attachedArticleSearchExecutor;

  @Autowired
  private AttachedArticleRequestBuilder attachedArticleRequestBuilder;


  public void addItemToCart(ConnectUser user, ArticleDocDto article, Optional<VehicleDto> vehicle,
      Integer quantity) {
    final ShoppingCartRequestBody request = new ShoppingCartRequestBody();
    request.setArticle(article);
    vehicle.ifPresent(request::setVehicle);
    request.setQuantity(quantity);
    request.setBasketItemSourceId(UUID.randomUUID().toString());
    request.setBasketItemSourceDesc(DVSE_BASKET_ITEM_SOURCE_DESC);

    cartManagerService.add(createCartItem(user, request));

    cartManagerService.updateTotalCache(user.getCachedUserId(), user.getCustNrStr(),
        ShopType.DEFAULT_SHOPPING_CART);

    /**
     * notify the total to the mini cart that item is added to cache and reload the minicart
     * submenu
     */
    if (isWebSocketEnabled()) {
      final int total = cartManagerService.getLatestTotalQuantity(
          ConnectUser.userKey(user.getCachedUserId(), user.getCustNrStr()),
          ShopType.DEFAULT_SHOPPING_CART);
      totalCartNotification.notifyQuickViewCart(user.getCachedUserId(), total);
    }
  }

  protected boolean isWebSocketEnabled() {
    return soapProps.getWebSocket().isOn();
  }

  private CartItemDto createCartItem(ConnectUser user, ShoppingCartRequestBody request) {
    final ArticleDocDto artDto = request.getArticle();
    final String cartKey = CartKeyGenerators.cartKey(user.key(), request.vehId(),
        request.idSagsys(), artDto.getSupplierId(), artDto.getSupplierArticleNumber(),
        ShopType.DEFAULT_SHOPPING_CART);

    final CartItemDto addedCartItem = cartManagerService.findByKey(user.key(), cartKey)
      .map(changeQuantity(request))
      .orElseGet(generateCartItem(cartKey, user, request));

    Optional.ofNullable(artDto)
    .filter(ArticleDocDto::hasAttachedArticle)
    .ifPresent(art -> updateAttachedArticles(user, addedCartItem));

    return addedCartItem;
  }

  private Function<CartItemDto, CartItemDto> changeQuantity(ShoppingCartRequestBody request) {
    return item -> {
      log.debug("Update article info with cart item");
      item.setQuantity(request.getQuantity());
      return item;
    };
  }

  private Supplier<CartItemDto> generateCartItem(final String cartKey,
    final ConnectUser user, final ShoppingCartRequestBody request) {
    return () -> {
      log.debug("Add new article info with cart item");
      return CartItemDtoBuilders.build(cartKey, user, request, ShopType.DEFAULT_SHOPPING_CART);
    };
  }

  private void updateAttachedArticles(ConnectUser user, CartItemDto addedCartItem) {

    final VehicleDto vehicle = addedCartItem.getVehicle();
    final List<AttachedArticleRequest> attachedAticleRequestList = attachedArticleRequestBuilder
        .buildAttachedAticleRequestList(addedCartItem, ShopType.DEFAULT_SHOPPING_CART);
    if (CollectionUtils.isEmpty(attachedAticleRequestList)) {
      return;
    }
    AttachedArticleSearchCriteria criteria =
        AttachedArticleSearchCriteria.createcreateArticleRequest(user.getAffiliate(),
            user.getCustomer(), attachedAticleRequestList, vehicle,
            user.getSettings().getWssDeliveryProfile());
    criteria.setPriceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum());
    criteria.setFinalCustomerUser(user.isFinalUserRole());
    criteria.setFinalCustomerHasNetPrice(user.isFinalCustomerHasNetPrice());
    Map<String, ArticleDocDto> attachedArticles = attachedArticleSearchExecutor.execute(criteria);
    if (MapUtils.isEmpty(attachedArticles)) {
      return;
    }

    addedCartItem
        .setAttachedArticles(attachedArticles.values().stream().collect(Collectors.toList()));
  }

}
