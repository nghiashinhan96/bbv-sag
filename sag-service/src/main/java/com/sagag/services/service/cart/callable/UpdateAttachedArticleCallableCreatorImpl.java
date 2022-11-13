package com.sagag.services.service.cart.callable;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.service.cart.attachedarticle.AttachedShoppingCartItemFilter;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UpdateAttachedArticleCallableCreatorImpl
    implements CallableCreator<List<ShoppingCartItem>, Void> {

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private AttachedArticleRequestBuilder attachedArticleRequestBuilder;

  @Autowired
  private List<AttachedShoppingCartItemFilter> attachedShoppingCartItemFilters;

  /**
   * Asynchronously update price for attached articles for shopping basket.<br/>
   * Since this method was used only for shopping basket viewing purpose.
   *
   * @param user the user who requests
   * @param vehDoc the vehicle in context, nullable
   * @param items the shopping basket item list
   * @return a {@link CompletableFuture} job with the results
   */
  @Override
  public Callable<Void> create(List<ShoppingCartItem> items,
      Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      final UserInfo user = (UserInfo) objects[1];
      final VehicleDto vehDoc = (VehicleDto) objects[2];
      final ShopType shopType = (ShopType) objects[3];

      final List<AttachedArticleRequest> attachedArticleRequest = items.stream()
          .map(ShoppingCartItem.entityConverter())
          .map(i -> attachedArticleRequestBuilder.buildAttachedAticleRequestList(i, shopType))
          .flatMap(List::stream)
          .collect(Collectors.toList());

      if (CollectionUtils.isEmpty(attachedArticleRequest)) {
        return null;
      }
      final Map<String, ArticleDocDto> attachedArticleMap =
          ivdsArticleTaskExecutors.executeTaskWithAttachedArticles(user, attachedArticleRequest,
              Optional.ofNullable(vehDoc));
      log.info("[BBV] create: Updated {} attached articles", attachedArticleMap.size());
      updateAttachedArticleForShoppingItems(user, items, attachedArticleMap, shopType);
      log.info("[BBV] updateAttachedArticleForShoppingItems: done {} shoppingCartItems", items.size());
      return null;
    };
  }

  private void updateAttachedArticleForShoppingItems(final UserInfo user,
      final Collection<ShoppingCartItem> items,
      final Map<String, ArticleDocDto> attachedArticleMap, final ShopType shopType) {
    final double vatRate = user.getSettings().getVatRate();

    for (final ShoppingCartItem item : items) {
      final ArticleDocDto articleDoc = item.getArticle();
      if (Objects.isNull(articleDoc) || !articleDoc.hasErpArticle()) {
        // There is no attached article in this item
        continue;
      }
      final List<ArticleDocDto> attachedArticles = new ArrayList<>();

      attachedShoppingCartItemFilters.forEach(filter -> filter.updateAttachedCartItemToBuckets(
          attachedArticles, user, item, vatRate, shopType, attachedArticleMap));

      item.setAttachedArticles(attachedArticles);
    }
  }

}
