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
@Order(3)
public class VocItemFilterImpl implements AttachedShoppingCartItemFilter {

  @Autowired
  private SupportedAttachedArticle supportedAttachedArticle;

  @Override
  public Optional<ShoppingCartItem> find(UserInfo user, ShoppingCartItem item, double vatRate,
      ShopType shopType, Map<String, ArticleDocDto> attachedArticleMap) {
    final ArticleDocDto articleDoc = item.getArticle();
    final String vehicleId = VehicleUtils.defaultVehId(item.getVehicleId());
    final String vocArtId = findVocArticleId(item);
    final String vocArticleKey = CartKeyGenerators.createUuidArticleKey(vehicleId,
        articleDoc.getArticle().getId(), vocArtId, shopType);
    final ArticleDocDto vocArticle = attachedArticleMap.get(vocArticleKey);
    final boolean hasVoc = vocArtId != null && vocArticle != null;
    if (!hasVoc) {
      return Optional.empty();
    }
    final int quantity = vocArticle.getAmountNumber();
    final ShoppingCartItem vocItem = new ShoppingCartItem(vocArticle, quantity, vatRate);
    vocItem.setPriceItem(vocArticle, vatRate);
    vocItem.setVoc(true);
    return Optional.of(vocItem);
  }

  @Override
  public Optional<String> findIdByErpArticle(Article erpArticle) {
    return supportedAttachedArticle.supportVoc(erpArticle)  && erpArticle.hasVocArticleId()
        ? Optional.of(erpArticle.getVocArticleId()) : Optional.empty();
  }

}
