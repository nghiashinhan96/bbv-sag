package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddMultipleCartItemShoppingCartOperation
    extends AbstractAddCartItemShoppingCartOperation<List<ArticleDocDto>> {

  @Autowired
  private DefaultAddCartItemShoppingCartOperation addCartItemShopCartOperation;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, List<ArticleDocDto> validArticles, ShopType shopType,
      Object... additionals) {
    final String basketItemSourceId =  (String) additionals[0];
    final String basketItemSourceDesc =  (String) additionals[1];
    final List<ShoppingCartRequestBody> requests = validArticles.stream().map(article -> {
      final ShoppingCartRequestBody cartRequest = new ShoppingCartRequestBody();
      cartRequest.setArticle(SagBeanUtils.map(article, ArticleDocDto.class));
      cartRequest.setBasketItemSourceId(basketItemSourceId);
      cartRequest.setBasketItemSourceDesc(basketItemSourceDesc);
      cartRequest.setQuantity(article.getAmountNumber());
      return cartRequest;
    }).collect(Collectors.toList());
    ShoppingCart shoppingCart = addCartItemShopCartOperation.execute(user, requests, shopType);
    couponBusService.validateCoupon(user, shoppingCart);

    return shoppingCart;
  }

}
