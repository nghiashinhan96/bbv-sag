package com.sagag.services.service.cart.attachedarticle;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface AttachedShoppingCartItemFilter {

  /**
   * Filters the attached shopping cart item in map.
   *
   * @param user
   * @param item
   * @param vatRate
   * @param shopType
   * @param attachedArticleMap
   * @return the optional result
   */
  Optional<ShoppingCartItem> find(UserInfo user, ShoppingCartItem item, double vatRate,
      ShopType shopType, Map<String, ArticleDocDto> attachedArticleMap);

  /**
   * Finds article id by ERP article.
   *
   * @param erpArticle
   * @return the optional of article id
   */
  Optional<String> findIdByErpArticle(Article erpArticle);

  /**
   * Update attached cart item to buckets.
   *
   * @param attachedArticles
   * @param user
   * @param item
   * @param vatRate
   * @param shopType
   * @param attachedArticleMap
   */
  default void updateAttachedCartItemToBuckets(List<ArticleDocDto> attachedArticles,
      UserInfo user, ShoppingCartItem item,
      double vatRate, ShopType shopType, Map<String, ArticleDocDto> attachedArticleMap) {
    find(user, item, vatRate, shopType, attachedArticleMap)
    .ifPresent(attatchedItem -> attachedArticles.add(attatchedItem.getArticle()));
  }

  default String findDepotArticleId(ShoppingCartItem item) {
    return findAttachedArticleId(item,
        i -> !StringUtils.isBlank(i.getArticle().getArticle().getDepotArticleId()),
        i -> i.getArticle().getArticle().getDepotArticleId());
  }

  default String findRecycleArticleId(ShoppingCartItem item) {
    return findAttachedArticleId(item,
        i -> !StringUtils.isBlank(i.getArticle().getArticle().getRecycleArticleId()),
        i -> i.getArticle().getArticle().getRecycleArticleId());
  }

  default String findVrgArticleId(ShoppingCartItem item) {
    return findAttachedArticleId(item,
        i -> !StringUtils.isBlank(i.getArticle().getArticle().getVrgArticleId()),
        i -> i.getArticle().getArticle().getVrgArticleId());
  }

  default String findVocArticleId(ShoppingCartItem item) {
    return findAttachedArticleId(item,
        i -> !StringUtils.isBlank(i.getArticle().getArticle().getVocArticleId()),
        i -> i.getArticle().getArticle().getVocArticleId());
  }

  static String findAttachedArticleId(ShoppingCartItem item,
    final Predicate<ShoppingCartItem> predicate,
    final Function<ShoppingCartItem, String> converter) {
    if (item == null || item.getArticle() == null || item.getArticle().getArticle() == null
        || !predicate.test(item)) {
      return null;
    }
    return converter.apply(item);
  }
}
