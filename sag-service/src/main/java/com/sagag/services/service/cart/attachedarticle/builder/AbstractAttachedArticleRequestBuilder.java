package com.sagag.services.service.cart.attachedarticle.builder;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.service.cart.attachedarticle.AttachedShoppingCartItemFilter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractAttachedArticleRequestBuilder
    implements AttachedArticleRequestBuilder {

  @Autowired
  private List<AttachedShoppingCartItemFilter> attachedCartItemFilters;

  @Override
  public List<AttachedArticleRequest> buildAttachedAticleRequestList(Object... objects) {
    final CartItemDto cartItem = (CartItemDto) objects[0];
    final ShopType shopType = (ShopType) objects[1];
    ArticleDocDto articleDoc = cartItem.getArticle();
    final Article erpArticle = articleDoc.getArticle();
    final Double inheritVatRate = Optional.ofNullable(articleDoc)
        .map(ArticleDocDto::getPrice).map(PriceWithArticle::getPrice)
        .map(PriceWithArticlePrice::getVatInPercent).orElse(NumberUtils.DOUBLE_ZERO);
    return extractAttachedArticleIds(erpArticle).stream()
        .map(id -> buildAttachedArticleRequest(cartItem, shopType, id, inheritVatRate))
        .collect(Collectors.toList());
  }

  @Override
  public List<AttachedArticleRequest> buildAttachedArticleDepositRequestList(
      List<ArticleDocDto> articles) {
    List<AttachedArticleRequest> requests = new LinkedList<>();
    CollectionUtils.emptyIfNull(articles).stream().forEach(art -> {
      Double inheritVatRate = Optional.ofNullable(art)
          .map(ArticleDocDto::getPrice).map(PriceWithArticle::getPrice)
          .map(PriceWithArticlePrice::getVatInPercent).orElse(NumberUtils.DOUBLE_ZERO);
      requests.addAll(extractAttachedArticleIds(art.getArticle()).stream()
          .map(id -> AttachedArticleRequest.of(id, art.getAmountNumber(), art.getSalesQuantity(),
              inheritVatRate))
          .collect(Collectors.toList()));
    });
    return requests;
  }

  private List<String> extractAttachedArticleIds(final Article erpArticle) {
    if (erpArticle == null) {
      return Collections.emptyList();
    }

    final List<String> attachedArticleIdList = new ArrayList<>();
    attachedCartItemFilters.forEach(
        filter -> filter.findIdByErpArticle(erpArticle).ifPresent(attachedArticleIdList::add));
    return attachedArticleIdList;
  }

  protected AttachedArticleRequest buildAttachedArticleRequest(CartItemDto cartItem,
      ShopType shopType, String attachedArticleId, Double vatRate) {
    final ArticleDocDto article = cartItem.getArticle();
    final VehicleDto vehicle = cartItem.getVehicle();
    final String vehicleId = AttachedArticleRequestBuilder.extractVehicleId(vehicle);

    final String artId = article.getIdSagsys();
    final int quantity = cartItem.getQuantity();
    final Integer salesQuantity = article.getSalesQuantity();
    return buildAttachedArtRequest(vehicleId, artId, attachedArticleId, shopType, quantity,
        salesQuantity, vatRate);
  }

  private static AttachedArticleRequest buildAttachedArtRequest(String vehicleId, String articleId,
      String attachedArticleId, ShopType shopType, int quantity, Integer salesQuantity,
      Double vatRate) {
    final String attachedCartKey =
        CartKeyGenerators.createUuidArticleKey(vehicleId, articleId, attachedArticleId, shopType);
    return AttachedArticleRequest.of(attachedCartKey, attachedArticleId, quantity, salesQuantity,
        vatRate);
  }

}
