package com.sagag.services.service.cart.attachedarticle.filter;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.attachedarticle.SupportedAttachedArticle;
import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.cart.attachedarticle.AttachedShoppingCartItemFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@Order(2)
public class RecycleItemFilterImpl implements AttachedShoppingCartItemFilter {

  @Autowired
  private SupportedAttachedArticle supportedAttachedArticle;

  @Override
  public Optional<ShoppingCartItem> find(UserInfo user, ShoppingCartItem item, double vatRate,
      ShopType shopType, Map<String, ArticleDocDto> attachedArticleMap) {
    final ArticleDocDto articleDoc = item.getArticle();
    final String vehicleId = VehicleUtils.defaultVehId(item.getVehicleId());
    final String recycleArtId = findRecycleArticleId(item);
    final String recycleKey = CartKeyGenerators.createUuidArticleKey(vehicleId,
        articleDoc.getArticle().getId(), recycleArtId, shopType);
    final ArticleDocDto recycleArt = attachedArticleMap.get(recycleKey);
    final boolean hasRecycle = recycleArtId != null && recycleArt != null;
    if (!hasRecycle) {
      return Optional.empty();
    }
    final int quantity = recycleArt.getAmountNumber();
    final ShoppingCartItem recycleItem = new ShoppingCartItem(recycleArt, quantity, vatRate);
    recycleItem.setPriceItem(recycleArt, vatRate);
    recycleItem.setRecycle(true);
    return Optional.of(recycleItem);
  }

  @Override
  public Optional<String> findIdByErpArticle(Article erpArticle) {
    return supportedAttachedArticle.supportRecycle(erpArticle) && erpArticle.hasRecycleArticleId()
        ? Optional.of(erpArticle.getRecycleArticleId()) : Optional.empty();
  }

}
