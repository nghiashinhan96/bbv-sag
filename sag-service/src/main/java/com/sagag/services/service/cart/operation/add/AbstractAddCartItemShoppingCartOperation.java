package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.BasketHistoryItemDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.ArticlesOrigin;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.executor.ThreadManager;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.service.cart.operation.AbstractShoppingCartOperation;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public abstract class AbstractAddCartItemShoppingCartOperation<T>
    extends AbstractShoppingCartOperation<T, ShoppingCart> {

  protected static final boolean NO_UPDATE_AVAIL_REQUEST = false;

  @Autowired
  private ThreadManager threadManager;

  @Autowired
  @Qualifier("createCartItemCallableCreatorImpl")
  protected CallableCreator<ShoppingCartRequestBody, Void> createCartItemCallableCreator;

  /**
   * @deprecated  using this.addCartItemIntoMainThread(...)
   * */
  @Deprecated
  protected CompletableFuture<Void> createCartItemJob(UserInfo user,
      ShoppingCartRequestBody cartRequest, Date addedTime, ShopType shopType,
      List<CartItemDto> items) {
    final ServletRequestAttributes mainThreadAttribute =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    Callable<Void> callable = createCartItemCallableCreator.create(cartRequest, mainThreadAttribute,
        user, addedTime, shopType, items);
    return threadManager.supplyAsyncVoid(callable);
  }

  protected void addCartItemIntoMainThread(UserInfo user,
    ShoppingCartRequestBody cartRequest, Date addedTime, ShopType shopType,
    List<CartItemDto> items) {
    try {
      final ServletRequestAttributes mainThreadAttribute =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      createCartItemCallableCreator.create(cartRequest, mainThreadAttribute,
        user, addedTime, shopType, items).call();
    } catch (Exception ex) {
      log.error("Adding cart item into main thread occur error: ", ex);
    }
  }

  protected Consumer<ShoppingCartRequestBody> addCreateCartItemJobConsumer(final UserInfo user,
      final Date addedTime, final ShopType shopType, final List<CartItemDto> items) {
    return cartRequest -> {
      // #4126 Every article NOT directly add from TECCAT Service (DvseCartService)
      // will be set Origin as from OTHER
      cartRequest.getArticle().setOrigin(ArticlesOrigin.FROM_OTHER);
      addCartItemIntoMainThread(user, cartRequest, addedTime, shopType, items);
    };
  }

  protected static Function<ArticleDocDto, ShoppingCartRequestBody> cartRequestConverter(
      final BasketHistoryItemDto item) {
    return article -> {
      final ShoppingCartRequestBody cartRequest = new ShoppingCartRequestBody();
      final VehicleDto vehicle = new VehicleDto();
      if (item.getVehicle() != null) {
        SagBeanUtils.copyProperties(item.getVehicle(), vehicle);
      }
      cartRequest.setVehicle(vehicle);
      cartRequest.setArticle(article);
      if (Objects.nonNull(article.getAmountNumber())) {
        cartRequest.setQuantity(article.getAmountNumber());
      }
      cartRequest.setBasketItemSourceId(article.getBasketItemSourceId());
      cartRequest.setBasketItemSourceDesc(article.getBasketItemSourceDesc());
      return cartRequest;
    };
  }

}
