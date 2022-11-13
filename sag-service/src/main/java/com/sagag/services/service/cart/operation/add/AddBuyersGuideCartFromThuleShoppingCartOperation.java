package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.ivds.api.IvdsThuleService;
import com.sagag.services.ivds.response.ThuleArticleListSearchResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AddBuyersGuideCartFromThuleShoppingCartOperation
  extends AbstractAddCartItemShoppingCartOperation<Map<String, String>> {

  private static final String BASKET_SOURCE_ITEM_ID = "basketItemSourceId";

  private static final String BASKET_SOURCE_ITEM_DESC = "basketItemSourceDesc";

  @Autowired
  private IvdsThuleService ivdsThuleService;

  @Autowired
  private AddMultipleCartItemShoppingCartOperation addMultipleCartItemShopCartOperation;

  @Override
  public ShoppingCart execute(UserInfo user, Map<String, String> buyersGuideFormData,
      ShopType shopType, Object... additionals) {
    final String basketItemSourceId =  buyersGuideFormData.getOrDefault(BASKET_SOURCE_ITEM_ID, StringUtils.EMPTY);
    final String basketItemSourceDesc =  buyersGuideFormData.getOrDefault(BASKET_SOURCE_ITEM_DESC, StringUtils.EMPTY);
    final ThuleArticleListSearchResponse response =
        ivdsThuleService.searchArticlesByBuyersGuide(user, buyersGuideFormData);
    if (!response.isPresent()) {
      throw new IllegalArgumentException("No items from Thule System is proceed");
    }

    final List<ArticleDocDto> articles = response.getThuleArticles();
    ShoppingCart shoppingCart = new ShoppingCart();
    if (!CollectionUtils.isEmpty(articles)) {
      final List<String> articleIdsFromThuleResponse = articles.stream()
          .map(ArticleDocDto::getArtid).distinct().collect(Collectors.toList());
      shoppingCart = addMultipleCartItemShopCartOperation.execute(user, articles, shopType,
      basketItemSourceId, basketItemSourceDesc);
      shoppingCart.getItems().removeIf(cartItem -> {
        if (cartItem.getArticleItem() == null) {
          return true;
        }
        return !articleIdsFromThuleResponse.contains(cartItem.getArticleItem().getArtid());
      });
    }

    Optional.ofNullable(response.getNotFoundPartNumbers())
    .filter(CollectionUtils::isNotEmpty)
    .ifPresent(shoppingCart::setNotFoundPartNumbers);
    return shoppingCart;
  }

}
