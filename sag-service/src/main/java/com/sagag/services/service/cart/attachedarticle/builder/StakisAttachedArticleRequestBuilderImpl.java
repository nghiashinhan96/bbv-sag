package com.sagag.services.service.cart.attachedarticle.builder;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.stakis.erp.domain.TmAttachedArticleRequest;

import org.springframework.stereotype.Component;

@Component
@CzProfile
public class StakisAttachedArticleRequestBuilderImpl extends AbstractAttachedArticleRequestBuilder {

  @Override
  protected AttachedArticleRequest buildAttachedArticleRequest(CartItemDto addedCartItem,
      ShopType shopType, String attachedArticleId, Double vatRate) {
    final VehicleDto vehicle = addedCartItem.getVehicle();
    final String vehicleId = AttachedArticleRequestBuilder.extractVehicleId(vehicle);
    final ArticleDocDto article = addedCartItem.getArticle();
    final Article erpArticle = article.getArticle();
    final String artId = article.getIdSagsys();
    final int quantity = addedCartItem.getQuantity();
    final Integer salesQuantity = article.getSalesQuantity();
    final String depotArticleId = erpArticle.getDepotArticleId();

    final String depotKey =
        CartKeyGenerators.createUuidArticleKey(vehicleId, artId, depotArticleId, shopType);
    final ArticleDocDto depositArticle = article.getDepositArticle();
    final TmAttachedArticleRequest request = new TmAttachedArticleRequest();
    request.setDepositArticle(depositArticle);
    request.setCartKey(depotKey);
    request.setAttachedArticleId(depotArticleId);
    request.setQuantity(quantity);
    request.setSalesQuantity(salesQuantity);
    return request;
  }

}
