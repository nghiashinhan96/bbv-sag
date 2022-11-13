package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.cart.CartItemUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UpdatePriceShoppingCartOperation extends DefaultFinalInfoShoppingCartOperation {

  @LogExecutionTime(infoMode = true)
  @Override
  public ShoppingCart execute(UserInfo user, Void criteria, ShopType shopType,
      Object... additionals) {
    final ShoppingCart shoppingCart = checkoutCart(user, shopType);
    final Map<String, VehicleDto> vehicleByVehId = getVehicleByVehId(shoppingCart);
    final Map<String, List<ShoppingCartItem>> artsByVehId = prepareSyncShoppingItems(shoppingCart);

    for (final Entry<String, List<ShoppingCartItem>> artEntry : artsByVehId.entrySet()) {
      final VehicleDto vehDoc = vehicleByVehId.get(artEntry.getKey());
      final List<ShoppingCartItem> items = artEntry.getValue();
      final List<ArticleDocDto> allDocs =
          items.stream().map(ShoppingCartItem::getArticleItem).collect(Collectors.toList());
      ivdsArticleTaskExecutors.executeTaskWithPrice(user, allDocs, Optional.ofNullable(vehDoc),
          Optional.empty());
    }

    final double vatRate = user.getSettings().getVatRate();
    CartItemUtils.bindAttachedArticleItems(shoppingCart, vatRate, user.getCustomer());

    doSyncLatestArticlesIntoCache(user.getCachedUserId(), user.getCustNrStr(), user.key(),
      shoppingCart, shopType, StringUtils.EMPTY, StringUtils.EMPTY);

    // Get newest cart info from cache after update
    return checkoutCart(user, shopType);
  }

}
