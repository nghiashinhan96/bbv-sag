package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.CustomShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.service.cart.operation.AbstractShoppingCartOperation;
import com.sagag.services.service.cart.operation.DefaultFinalInfoShoppingCartOperation;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AddCartItemsFromArtNumbersShoppingCartOperation
    extends AbstractShoppingCartOperation<String[], CustomShoppingCart> {

  @Autowired
  private IvdsArticleService ivdsArticleService;

  @Autowired
  @Qualifier("defaultFinalInfoShoppingCartOperation")
  private DefaultFinalInfoShoppingCartOperation finalInfoShopCartOperation;

  @Autowired
  private DefaultAddCartItemShoppingCartOperation addCartItemShopCartOperation;

  @Override
  @LogExecutionTime
  public CustomShoppingCart execute(UserInfo user, String[] articleNrs, ShopType shopType,
      Object... additionals) {
    Assert.notEmpty(articleNrs, "The given list of article numbers must not be empty");
    Assert.notEmpty(additionals, "The given list of quantities must not be empty");
    final Integer[] quantities = (Integer[]) additionals;

    // Search article numbers
    final List<String> articleNumbers = Stream.of(articleNrs).collect(Collectors.toList());
    final List<Integer> articleQuantities = Stream.of(quantities).collect(Collectors.toList());

    final Map<String, Integer> articleNumberQuantityMap = new HashMap<>();
    for (int index = 0; index < articleNumbers.size(); index++) {
      articleNumberQuantityMap.put(articleNumbers.get(index), articleQuantities.get(index));
    }

    final List<ArticleDocDto> validArticles = new ArrayList<>();
    final List<String> notValidArticleNumbers = new ArrayList<>();

    articleNumberQuantityMap.forEach((articleNr, quantity) -> {
      // Call service as deep link
      final Page<ArticleDocDto> articles =
          ivdsArticleService.searchArticlesByNumber(user, articleNr, quantity, PageUtils.DEF_PAGE,
              true);
      if (CollectionUtils.isEmpty(articles.getContent())) {
        notValidArticleNumbers.add(articleNr);
      } else {
        validArticles.addAll(articles.getContent());
      }
    });

    if (CollectionUtils.isEmpty(validArticles)) {
      return new CustomShoppingCart(finalInfoShopCartOperation.execute(user, null, shopType, true),
          notValidArticleNumbers);
    }

    // Add to shopping cart
    final ShoppingCart shoppingCart = addMultipleArticlesToCart(user, validArticles, shopType);

    return new CustomShoppingCart(shoppingCart, notValidArticleNumbers);
  }

  private ShoppingCart addMultipleArticlesToCart(final UserInfo user,
      final List<ArticleDocDto> validArticles, ShopType shopType) {

    final List<ShoppingCartRequestBody> requests = validArticles.stream().map(article -> {
      final ShoppingCartRequestBody cartRequest = new ShoppingCartRequestBody();
      cartRequest.setArticle(SagBeanUtils.map(article, ArticleDocDto.class));
      cartRequest.setQuantity(article.getAmountNumber());
      return cartRequest;
    }).collect(Collectors.toList());

    ShoppingCart shoppingCart = addCartItemShopCartOperation.execute(user, requests, shopType);
    couponBusService.validateCoupon(user, shoppingCart);
    return shoppingCart;
  }
}
