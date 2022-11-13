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
@Order(4)
public class VrgItemFilterImpl implements AttachedShoppingCartItemFilter {

  @Autowired
  private SupportedAttachedArticle supportedAttachedArticle;

  @Override
  public Optional<ShoppingCartItem> find(UserInfo user, ShoppingCartItem item, double vatRate,
      ShopType shopType, Map<String, ArticleDocDto> attachedArticleMap) {
    final ArticleDocDto articleDoc = item.getArticle();
    final String vehicleId = VehicleUtils.defaultVehId(item.getVehicleId());
    final String vrgArtId = findVrgArticleId(item);
    final String vrgArticleKey = CartKeyGenerators.createUuidArticleKey(vehicleId,
        articleDoc.getArticle().getId(), vrgArtId, shopType);
    final ArticleDocDto vrgArticle = attachedArticleMap.get(vrgArticleKey);
    final boolean hasVrg = vrgArtId != null && vrgArticle != null;
    if (!hasVrg) {
      return Optional.empty();
    }
    final int quantity = vrgArticle.getAmountNumber();
    final ShoppingCartItem vrgItem = new ShoppingCartItem(vrgArticle, quantity, vatRate);
    vrgItem.setPriceItem(vrgArticle, vatRate);
    vrgItem.setVrg(true);
    return Optional.of(vrgItem);
  }

  @Override
  public Optional<String> findIdByErpArticle(Article erpArticle) {
    return supportedAttachedArticle.supportVrg(erpArticle)  && erpArticle.hasVrgArticleId()
        ? Optional.of(erpArticle.getVrgArticleId()) : Optional.empty();
  }

}
