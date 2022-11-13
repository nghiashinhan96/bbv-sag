package com.sagag.services.service.cart;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@Component
public class CartResetDisplayPriceHelper implements BiConsumer<Boolean, ShoppingCart> {

  @Override
  public void accept(Boolean allowPriceDisplayChanged, ShoppingCart shoppingCart) {

    List<ShoppingCartItem> items = shoppingCart.getItems();
    if (BooleanUtils.isTrue(allowPriceDisplayChanged) || CollectionUtils.isEmpty(items)) {
      return;
    }
    items.forEach(item -> resetDisplayPrice(item.getArticleItem()));
    items.stream().filter(ShoppingCartItem::hasAttachedCartItems)
        .flatMap(item -> item.getAttachedArticles().stream()).forEach(this::resetDisplayPrice);
  }

  private void resetDisplayPrice(ArticleDocDto article) {
    if (Objects.nonNull(article)) {
      article.setDisplayedPrice(null);
    }
  }
}
