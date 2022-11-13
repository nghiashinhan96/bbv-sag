package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.ivds.executor.IvdsArticleAmountTaskExecutor;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AddArticlesByQuantityMapShoppingCartOperation
    extends AbstractAddCartItemShoppingCartOperation<Map<String, Integer>> {

  @Autowired
  private IvdsArticleAmountTaskExecutor ivdsArticleAmountTaskExecutor;

  @Autowired
  private AddMultipleCartItemShoppingCartOperation addMultipleCartItemShopCartOperation;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, Map<String, Integer> articleIdQuantityMap,
      ShopType shopType, Object... additionals) {
    long start = System.currentTimeMillis();
    final String basketItemSourceId =  (String) additionals[0];
    final String basketItemSourceDesc =  (String) additionals[1];
    final List<ArticleDocDto> validArticles = new ArrayList<>();
    articleIdQuantityMap.forEach((articleId, quantity) -> {
      // Call service as deep link
      final Page<ArticleDocDto> articles =
          ivdsArticleAmountTaskExecutor.execute(user, articleId, quantity, Optional.empty());
      validArticles.addAll(articles.getContent());
    });
    log.debug(
        "Perf:CartBusinessServiceImpl->addExternalOrderToCart-> search articles by amount {} ms",
        System.currentTimeMillis() - start);
    if (CollectionUtils.isEmpty(validArticles)) {
      throw new IllegalArgumentException("There is no valid article to add");
    }

    return addMultipleCartItemShopCartOperation.execute(user, validArticles,
        shopType, basketItemSourceId, basketItemSourceDesc);
  }

}
