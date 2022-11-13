package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.service.cart.CartItemUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateAttachedArticleShoppingCartOperation
    extends AbstractShoppingCartOperation<CartItemDto, Void> {

  @Autowired
  private AttachedArticleRequestBuilder attachedArticleRequestBuilder;

  @Override
  @LogExecutionTime(infoMode = true)
  public Void execute(UserInfo user, CartItemDto addedCartItem, ShopType shopType,
      Object... additionals) {
    this.updateAttachedArticleInfo(user, addedCartItem, shopType);
    return null;
  }

  private void updateAttachedArticleInfo(UserInfo user, CartItemDto addedCartItem,
      ShopType shopType) {
    final ArticleDocDto article = addedCartItem.getArticle();
    final VehicleDto vehicle = addedCartItem.getVehicle();
    List<ArticleDocDto> attachedArticles = new ArrayList<>();
    boolean isPfandArticle = CartItemUtils.showPfandArticleCase(user.getCustomer(), article);
    if (isPfandArticle) {
      ArticleDocDto pfandArticle = CartItemUtils.createPfandArticleDto(article.getIdSagsys(),
          article.getSalesQuantity(), addedCartItem.getQuantity());
      pfandArticle.setPrice(AxArticleUtils.createPricePfandArticle(addedCartItem.getQuantity(),
          AxPriceUtils.defaultVatRate(article, user.getSettings().getVatRate())));
      attachedArticles.add(pfandArticle);
    }

    final List<AttachedArticleRequest> attachedArticleRequestList =
        attachedArticleRequestBuilder.buildAttachedAticleRequestList(addedCartItem, shopType);

    if (CollectionUtils.isEmpty(attachedArticleRequestList)) {
      addedCartItem.setAttachedArticles(attachedArticles);
      return;
    }
    final Map<String, ArticleDocDto> attachedArticleMap =
        ivdsArticleTaskExecutors.executeTaskWithAttachedArticles(user, attachedArticleRequestList,
            Optional.ofNullable(vehicle));
    if (MapUtils.isEmpty(attachedArticleMap)) {
      return;
    }
    attachedArticles.addAll(attachedArticleMap.values().stream().collect(Collectors.toList()));

    addedCartItem.setAttachedArticles(attachedArticles);
  }

}
