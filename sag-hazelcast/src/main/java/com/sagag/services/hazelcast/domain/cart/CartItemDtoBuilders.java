package com.sagag.services.hazelcast.domain.cart;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Optional;

@UtilityClass
public class CartItemDtoBuilders {

  public CartItemDto build(String cartKey, UserInfo user, ShoppingCartRequestBody cartRequest,
      ShopType shopType) {
    CartItemDto item = new CartItemDto();
    item.setCartKey(cartKey);
    item.setCustomerNr(user.getCustNrStr());
    item.setUserId(user.getCachedUserId());
    item.setUserName(user.getCachedUsername());
    item.setArticle(cartRequest.getArticle());
    item.setQuantity(cartRequest.getQuantity());
    item.setCategory(cartRequest.getCategory());
    item.setVehicle(cartRequest.getVehicle());
    item.setAddedTime(Calendar.getInstance().getTime());
    item.setShopType(ShopType.defaultValueOf(shopType));
    item.setBasketItemSourceId(cartRequest.getBasketItemSourceId());
    item.setBasketItemSourceDesc(cartRequest.getBasketItemSourceDesc());
    // #6753 Add missing info for VIN package
    if (cartRequest.getArticle().isVin()) {
      item.setItemDesc(cartRequest.getArticle().getItemDesc());
    }
    Optional.ofNullable(cartRequest.getFinalCustomerOrgId())
    .map(Long::intValue).ifPresent(item::setFinalCustomerOrgId);

    updateCartItemType(user, item, cartRequest);
    return item;
  }

  private static void updateCartItemType(UserInfo user, final CartItemDto addedCartItem,
      final ShoppingCartRequestBody cartRequest) {
    if (cartRequest.hasLicense()) {
      final LicenseSettingsDto license = cartRequest.getLicense();
      addedCartItem
          .setItemDesc(StringUtils.defaultIfEmpty(license.getProductText(), license.getPackName()));
      addedCartItem.setItemType(CartItemType.VIN);
    } else {
      addedCartItem.setItemType(findCartItemType(user, addedCartItem));
    }
    // #6753 Add missing info for VIN package
    if (cartRequest.getArticle().isVin()) {
      addedCartItem.setItemType(CartItemType.VIN);
    }
  }

  private static CartItemType findCartItemType(UserInfo user, CartItemDto item) {
    if (user.isFinalUserRole() || item.getShopType() == ShopType.SUB_FINAL_SHOPPING_CART) {
      return CartItemType.ARTICLE;
    }
    final ArticleDocDto article = item.getArticle();
    if (article == null || StringUtils.isBlank(article.getArtid())) {
      return CartItemType.DVSE_NON_REF_ARTICLE;
    }
    return CartItemType.ARTICLE;
  }

}
