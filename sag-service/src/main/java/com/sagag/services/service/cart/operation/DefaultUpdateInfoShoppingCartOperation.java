package com.sagag.services.service.cart.operation;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.executor.ThreadManager;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.cart.callable.UpdateAttachedArticleCallableCreatorImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class DefaultUpdateInfoShoppingCartOperation
    extends AbstractShoppingCartOperation<ShoppingCart, ShoppingCart> {

  @Autowired
  private ThreadManager threadManager;

  @Autowired
  private UpdateAttachedArticleCallableCreatorImpl updateAttachedArticleCallableCreator;

  @Override
  public ShoppingCart execute(UserInfo user, ShoppingCart shoppingCart, ShopType shopType,
      Object... additionals) {
    final String vehId = (String) additionals[0];
    final String idSagSys = (String) additionals[1];
    final boolean isAvaiReq = (boolean) additionals[2];
    log.debug("Synchronizing shopping cart info, isSyncAvailability = {}", isAvaiReq);

    final List<CompletableFuture<Void>> callables = new ArrayList<>();
    if (StringUtils.isBlank(vehId) || StringUtils.isBlank(idSagSys)) {
      doUpdateAllCartItems(user, callables, shoppingCart, isAvaiReq, shopType);
    } else {
      doUpdateSelectedCartItem(user, callables, shoppingCart, isAvaiReq, vehId, idSagSys, shopType);
    }

    CompletableFuture.allOf(callables.toArray(new CompletableFuture[callables.size()])).join();

    // Async callings the update apis
    final double defaultVatRate = user.getSettings().getVatRate();
    updateArticleInformation(shoppingCart, defaultVatRate, user);

    doSyncLatestArticlesIntoCache(user.getCachedUserId(), user.getCustNrStr(), user.key(),
      shoppingCart, shopType, vehId, idSagSys);

    return shoppingCart;
  }

  private void doUpdateSelectedCartItem(final UserInfo user,
      final List<CompletableFuture<Void>> callables,
      final ShoppingCart shoppingCart, final boolean isAvaiReq,
      final String vehId, final String idSagSys, final ShopType shopType) {
    final ServletRequestAttributes mainThreadAttribute =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    final Map<String, VehicleDto> vehicleByVehId = getVehicleByVehId(shoppingCart);
    final Map<String, List<ShoppingCartItem>> artsByVehId = prepareSyncShoppingItems(shoppingCart);

    for (final Entry<String, List<ShoppingCartItem>> artEntry : artsByVehId.entrySet()) {
      final VehicleDto vehDoc = vehicleByVehId.get(artEntry.getKey());

      final List<ShoppingCartItem> items = artEntry.getValue();

      Callable<Void> callable = updateAttachedArticleCallableCreator.create(items,
          mainThreadAttribute, user, vehDoc, shopType);
      callables.add(threadManager.supplyAsyncVoid(callable));

      final List<ArticleDocDto> artDocs = findSyncArticles(vehId, idSagSys, vehDoc, items);
      if (!CollectionUtils.isEmpty(artDocs)) {
        ivdsArticleTaskExecutors.executeTaskWithPriceAndStock(user, artDocs,
            Optional.ofNullable(vehDoc), Optional.ofNullable(shoppingCart.getFinalCustomerOrgId()),
            Optional.empty());
      }
    }
    doUpdateAvailabilities(user, artsByVehId, isAvaiReq);
  }

  private static List<ArticleDocDto> findSyncArticles(final String vehId, final String idSagSys,
      VehicleDto vehDto, List<ShoppingCartItem> items) {
    final boolean matchedArt = isMatchedVehicle(vehId, vehDto);
    if (!matchedArt) {
      return Collections.emptyList();
    }
    // since the price request for license & article is not the same.
    return items.stream()
        .filter(i -> StringUtils.equals(i.getArticleItem().getIdSagsys(), idSagSys))
        .map(ShoppingCartItem::getArticleItem).collect(Collectors.toList());
  }

  private static boolean isMatchedVehicle(final String vehId, final VehicleDto vehDto) {
    if (Objects.isNull(vehDto)) {
      return false;
    }
    return StringUtils.equalsIgnoreCase(vehDto.getVehId(), vehId)
        || StringUtils.equalsIgnoreCase(StringUtils.defaultIfBlank(vehDto.getVehId(),
            SagConstants.KEY_NO_VEHICLE), SagConstants.KEY_NO_VEHICLE);
  }

  private void doUpdateAvailabilities(final UserInfo user,
      final Map<String, List<ShoppingCartItem>> artsByVehId, final boolean isAvaiReq) {
    if (!isAvaiReq || MapUtils.isEmpty(artsByVehId)) {
      return;
    }
    final List<ArticleDocDto> articles =
        artsByVehId.values().stream().flatMap(List::stream)
            .map(ShoppingCartItem::getArticleItem).collect(Collectors.toList());
    ivdsArticleTaskExecutors.executeTaskWithAvailabilityAndIgnoredArtElasticsearch(
        user, articles, Optional.empty(), Optional.empty());
  }

  private void doUpdateAllCartItems(final UserInfo user,
      final List<CompletableFuture<Void>> callables, ShoppingCart shoppingCart,
      final boolean isAvaiReq,
      final ShopType shopType) {

    final ServletRequestAttributes mainThreadAttribute =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    final Map<String, VehicleDto> vehicleByVehId = getVehicleByVehId(shoppingCart);
    final Map<String, List<ShoppingCartItem>> artsByVehId = prepareSyncShoppingItems(shoppingCart);
    final Optional<Integer> finalCustomerOrgIdOpt =
        Optional.ofNullable(shoppingCart.getFinalCustomerOrgId());

    log.info("[BBV] doUpdateAllCartItems: Retrieve artsByVehId map: {} items", artsByVehId.size());
    for (final Entry<String, List<ShoppingCartItem>> artEntry : artsByVehId.entrySet()) {
      final VehicleDto vehDoc = vehicleByVehId.get(artEntry.getKey());
      log.info("[BBV] doUpdateAllCartItems: Loop VehicleDTO: {}", vehDoc.getVehId());

      final List<ShoppingCartItem> items = artEntry.getValue();
      Callable<Void> arts = updateAttachedArticleCallableCreator.create(items,
          mainThreadAttribute, user, vehDoc, shopType);
      callables.add(threadManager.supplyAsyncVoid(arts));

      final List<ArticleDocDto> allDocs =
          items.stream().map(ShoppingCartItem::getArticleItem).collect(Collectors.toList());
      log.info("[BBV] doUpdateAllCartItems: Start executeTaskWithPriceAndStock: {}", allDocs.size());
      ivdsArticleTaskExecutors.executeTaskWithPriceAndStock(user, allDocs,
          Optional.ofNullable(vehDoc), finalCustomerOrgIdOpt, Optional.empty());
    }
    log.info("[BBV] doUpdateAllCartItems: End the loop, start doUpdateAvailabilities");
    doUpdateAvailabilities(user, artsByVehId, isAvaiReq);
    log.info("[BBV] doUpdateAllCartItems: End doUpdateAvailabilities");
  }

}
