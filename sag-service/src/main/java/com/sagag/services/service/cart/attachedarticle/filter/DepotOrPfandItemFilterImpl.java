package com.sagag.services.service.cart.attachedarticle.filter;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.attachedarticle.SupportedAttachedArticle;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.cart.CartItemUtils;
import com.sagag.services.service.cart.attachedarticle.AttachedShoppingCartItemFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@Order(1)
public class DepotOrPfandItemFilterImpl implements AttachedShoppingCartItemFilter {

  @Autowired
  private SupportedAttachedArticle supportedAttachedArticle;

  @Override
  public Optional<ShoppingCartItem> find(UserInfo user, ShoppingCartItem item, double vatRate,
      ShopType shopType, Map<String, ArticleDocDto> attachedArticleMap) {
    final ArticleDocDto articleDoc = item.getArticle();
    final String vehicleId = VehicleUtils.defaultVehId(item.getVehicleId());
    final String depotArtId = findDepotArticleId(item);
    final String depotKey = CartKeyGenerators.createUuidArticleKey(vehicleId,
        articleDoc.getArticle().getId(), depotArtId, shopType);
    ArticleDocDto depotArt = attachedArticleMap.get(depotKey);
    final boolean hasDepot = depotArtId != null && depotArt != null;
    final boolean isPfand = CartItemUtils.showPfandArticleCase(user.getCustomer(), articleDoc);
    if (!hasDepot && !isPfand) {
      return Optional.empty();
    }
    if (isPfand && !hasDepot) {
      depotArt = CartItemUtils.createPfandArticleDto(articleDoc.getIdSagsys(),
          articleDoc.getSalesQuantity(), item.getQuantity());
      depotArt.setPrice(AxArticleUtils.createPricePfandArticle(item.getQuantity(),
          AxPriceUtils.defaultVatRate(articleDoc, user.getSettings().getVatRate())));
    }
    final int quantity = depotArt.getAmountNumber();
    final ShoppingCartItem depotItem = new ShoppingCartItem(depotArt, quantity, vatRate);
    depotItem.setPriceItem(depotArt, vatRate);
    depotItem.setDepot(true);
    depotItem.setPfand(isPfand);
    return Optional.of(depotItem);
  }

  @Override
  public Optional<String> findIdByErpArticle(Article erpArticle) {
    return supportedAttachedArticle.supportDepot(erpArticle) && erpArticle.hasDepotArticleId()
        ? Optional.of(erpArticle.getDepotArticleId()) : Optional.empty();
  }

}
